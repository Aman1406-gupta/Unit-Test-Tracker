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
            report.setSuiteName(suiteElement.getAttribute("suitename"));
            report.setTotalTests(parseIntSafe(suiteElement.getAttribute("tests")));
            report.setTotalErrors(parseIntSafe(suiteElement.getAttribute("errors")));
            report.setTotalFailures(parseIntSafe(suiteElement.getAttribute("failures")));
            report.setTotalSkipped(parseIntSafe(suiteElement.getAttribute("skipped")));

            Map<String, ParsedTestClass> classMap = new LinkedHashMap<>();
            NodeList testcaseNodes = doc.getElementsByTagName("testcase");

            for (int i = 0; i < testcaseNodes.getLength(); i++) {
                Node node = testcaseNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element testcase = (Element) node;

                String className = testcase.getAttribute("className");
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
                    methodName = testcase.getAttribute("name");
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
                testCase.setTimestamp_execution(Instant.parse(ts));

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
            report.setTestClasses(classes);

            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JUnit XML report", e);
        }
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
}