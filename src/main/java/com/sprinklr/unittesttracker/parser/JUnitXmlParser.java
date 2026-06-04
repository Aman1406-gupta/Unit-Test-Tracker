package com.sprinklr.unittesttracker.parser;

import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class JUnitXmlParser {

    public ParsedTestReport parse(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setExpandEntityReferences(false);
            Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
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
            report.setSuiteName(suiteElement.getAttribute("name"));
            report.setBuildID_suite(suiteElement.getAttribute("buildID_suite"));
            report.setCommitID_suite(suiteElement.getAttribute("commitID_suite"));
            report.setBranchName_suite(suiteElement.getAttribute("branchName_suite"));
            report.setTests(parseIntSafe(suiteElement.getAttribute("tests")));
            report.setFailures(parseIntSafe(suiteElement.getAttribute("failures")));
            report.setErrors(parseIntSafe(suiteElement.getAttribute("errors")));
            report.setSkipped(parseIntSafe(suiteElement.getAttribute("skipped")));
            report.setTimestamp_suite(parseInstantSafe(suiteElement.getAttribute("timestamp")));

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
                testCase.setTestName(testcase.getAttribute("name"));
                testCase.setTestClass(className);
                testCase.setDuration(parseDoubleSafe(testcase.getAttribute("time")));
                testCase.setTimestamp(report.getTimestamp_suite() != null ? report.getTimestamp_suite() : Instant.now());

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
        double duration = 0.0;

        for (ParsedTestCase testCase : parsedClass.getTestCases()) {
            tests++;
            duration += testCase.getDuration();

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
        parsedClass.setDuration(duration);
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