package com.sprinklr.unittesttracker.parser;

import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class JUnitXmlParser {

    public ParsedTestReport parseFiles(MultipartFile reportFile, MultipartFile metadataFile) {
        try {
            String xmlContent = new String(reportFile.getBytes(), StandardCharsets.UTF_8);
            return parse(xmlContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read XML report file", e);
        }
    }

    public ParsedTestReport parse(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setExpandEntityReferences(false);

            Document doc = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            Element suiteElement = root;

            if ("testsuites".equalsIgnoreCase(root.getTagName())) {
                NodeList suites = root.getElementsByTagName("testsuite");
                if (suites.getLength() > 0 && suites.item(0).getNodeType() == Node.ELEMENT_NODE) {
                    suiteElement = (Element) suites.item(0);
                }
            }

            ParsedTestReport report = new ParsedTestReport();

            // metadata will be attached later by MetadataParser
            report.setMetadata(null);

            // suite-level fields
            String suiteName = suiteElement.getAttribute("suiteName");
            if (suiteName == null || suiteName.isBlank()) {
                suiteName = suiteElement.getAttribute("name");
            }
            report.setSuiteName(suiteName);

            Map<String, ParsedTestClass> classMap = new LinkedHashMap<>();
            NodeList testcaseNodes = doc.getElementsByTagName("testcase");

            for (int i = 0; i < testcaseNodes.getLength(); i++) {
                Node node = testcaseNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element testcase = (Element) node;

                String className = testcase.getAttribute("classname");
                if (className == null || className.isBlank()) {
                    className = "UnknownClass";
                }

                ParsedTestClass parsedClass = classMap.computeIfAbsent(className, key -> {
                    ParsedTestClass c = new ParsedTestClass();
                    c.setClassName(key);
                    return c;
                });

                ParsedTestCase testCase = new ParsedTestCase();

                String testName = testcase.getAttribute("testName");
                if (testName == null || testName.isBlank()) {
                    testName = testcase.getAttribute("name");
                }
                testCase.setTestName(testName);

                String methodName = testcase.getAttribute("method");
                if (methodName == null || methodName.isBlank()) {
                    methodName = testcase.getAttribute("methodName");
                }
                testCase.setMethodName(methodName);

                String duration = testcase.getAttribute("duration");
                if (duration == null || duration.isBlank()) {
                    duration = testcase.getAttribute("time");
                }
                testCase.setDuration(parseDoubleSafe(duration));

                String ts = testcase.getAttribute("timestamp_execution");
                if (ts == null || ts.isBlank()) {
                    ts = testcase.getAttribute("timestamp");
                }
                testCase.setTimestamp_execution(parseInstantSafe(ts));

                String status = "PASSED";
                String errorMessage = null;
                String stackTrace = null;

                NodeList children = testcase.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if (child.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Element childEl = (Element) child;
                    String tagName = childEl.getTagName();

                    if ("failure".equalsIgnoreCase(tagName) || "error".equalsIgnoreCase(tagName)) {
                        status = "FAILED";
                        errorMessage = childEl.getAttribute("message");
                        if (errorMessage == null || errorMessage.isBlank()) {
                            errorMessage = childEl.getTextContent();
                        }
                        stackTrace = childEl.getTextContent();
                    } else if ("skipped".equalsIgnoreCase(tagName)) {
                        status = "SKIPPED";
                    }
                }

                testCase.setStatus(status);
                testCase.setErrorMessage(errorMessage);
                testCase.setStackTrace(stackTrace);

                parsedClass.getTestCases().add(testCase);
            }

            List<ParsedTestClass> classes = new ArrayList<>(classMap.values());
            for (ParsedTestClass c : classes) {
                recalculateClassSummary(c);
            }

            report.setTestClasses(classes);
            recalculateSuiteSummary(report, suiteElement);

            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JUnit XML report", e);
        }
    }

    private void recalculateClassSummary(ParsedTestClass parsedClass) {
        int tests = 0;
        int failures = 0;
        int errors = 0;
        int skipped = 0;

        for (ParsedTestCase testCase : parsedClass.getTestCases()) {
            tests++;

            if ("FAILED".equalsIgnoreCase(testCase.getStatus())) {
                failures++;
            } else if ("SKIPPED".equalsIgnoreCase(testCase.getStatus())) {
                skipped++;
            } else if ("ERROR".equalsIgnoreCase(testCase.getStatus())) {
                errors++;
            }
        }

        parsedClass.setTests(tests);
        parsedClass.setFailures(failures);
        parsedClass.setErrors(errors);
        parsedClass.setSkipped(skipped);
    }

    private void recalculateSuiteSummary(ParsedTestReport parsedReport, Element suiteElement) {
        int totalTests = 0;
        int totalFailures = 0;
        int totalErrors = 0;
        int totalSkipped = 0;

        for (ParsedTestClass testClass : parsedReport.getTestClasses()) {
            totalTests += testClass.getTests();
            totalFailures += testClass.getFailures();
            totalErrors += testClass.getErrors();
            totalSkipped += testClass.getSkipped();
        }

        int declaredTests = parseIntSafe(firstNonBlank(
                suiteElement.getAttribute("totalTests"),
                suiteElement.getAttribute("tests")
        ));
        int declaredFailures = parseIntSafe(firstNonBlank(
                suiteElement.getAttribute("totalFailures"),
                suiteElement.getAttribute("failures")
        ));
        int declaredErrors = parseIntSafe(firstNonBlank(
                suiteElement.getAttribute("totalErrors"),
                suiteElement.getAttribute("errors")
        ));
        int declaredSkipped = parseIntSafe(firstNonBlank(
                suiteElement.getAttribute("totalSkipped"),
                suiteElement.getAttribute("skipped")
        ));

        parsedReport.setTotalTests(declaredTests == totalTests || declaredTests == 0 ? totalTests : declaredTests);
        parsedReport.setTotalFailures(declaredFailures == totalFailures || declaredFailures == 0 ? totalFailures : declaredFailures);
        parsedReport.setTotalErrors(declaredErrors == totalErrors || declaredErrors == 0 ? totalErrors : declaredErrors);
        parsedReport.setTotalSkipped(declaredSkipped == totalSkipped || declaredSkipped == 0 ? totalSkipped : declaredSkipped);
    }

    private String firstNonBlank(String a, String b) {
        if (a != null && !a.isBlank()) return a;
        if (b != null && !b.isBlank()) return b;
        return null;
    }

    private int parseIntSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? 0 : Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? 0.0 : Double.parseDouble(value.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Instant parseInstantSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? Instant.now() : Instant.parse(value.trim());
        } catch (Exception e) {
            return Instant.now();
        }
    }
}