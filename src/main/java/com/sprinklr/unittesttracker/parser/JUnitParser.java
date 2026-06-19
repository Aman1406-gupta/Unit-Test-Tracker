package com.sprinklr.unittesttracker.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JUnitParser {

    public ParsedTestReport parseFiles(MultipartFile reportFile, MultipartFile testInfoFile) {
        try {
            String xmlContent = new String(reportFile.getBytes(), StandardCharsets.UTF_8);
            String testInfoContent = new String(testInfoFile.getBytes(), StandardCharsets.UTF_8);
            return parse(xmlContent, testInfoContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to Parse JUnit report and test info file", e);
        }
    }

    public ParsedTestReport parse(String xmlContent, String testInfoContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xmldoc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            xmldoc.getDocumentElement().normalize();

            Element root = xmldoc.getDocumentElement();
            String suiteName = root.getAttribute("suiteName");
            if (suiteName == null || suiteName.isBlank()) {
                suiteName = "UnknownSuite";
            }
            String buildID = root.getAttribute("buildID");
            if (buildID == null || buildID.isBlank()) {
                buildID = "UnknownBuildID";
            }
            String repositoryUrl = root.getAttribute("repositoryUrl");
            if (repositoryUrl == null || repositoryUrl.isBlank()) {
                repositoryUrl = "UnknownRepositoryUrl";
            }
            String branchName = root.getAttribute("branchName");
            if (branchName == null || branchName.isBlank()) {
                branchName = "UnknownBranchName";
            }
            String jobName = root.getAttribute("jobName");
            if (jobName == null || jobName.isBlank()) {
                jobName = "UnknownJobName";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode testInfoRoot = objectMapper.readTree(testInfoContent.getBytes(StandardCharsets.UTF_8));

            ParsedTestReport report = new ParsedTestReport();
            report.setSuiteName(suiteName);
            report.setBuildID(buildID);
            report.setRepository_url(repositoryUrl);
            report.setBranchName(branchName);
            report.setJobName(jobName);

            Map<String, ParsedTestClass> classMap = new LinkedHashMap<>();
            NodeList testclassNodes = root.getElementsByTagName("testclass");

            for (int i = 0; i < testclassNodes.getLength(); i++) {
                Node classNode = testclassNodes.item(i);
                if (classNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element testclassElement = (Element) classNode;
                String className = testclassElement.getAttribute("classname");
                if (className == null || className.isBlank()) {
                    className = "UnknownClass";
                }

                final String finalClassName = className;
                String simpleClassName = className.contains(".") ? className.substring(className.lastIndexOf('.') + 1) : className;

                ParsedTestClass parsedClass = classMap.computeIfAbsent(className, key -> {
                    ParsedTestClass c = new ParsedTestClass();
                    c.setClassName(key);
                    c.setTestCases(new ArrayList<>());
                    return c;
                });

                NodeList testcaseNodes = testclassElement.getElementsByTagName("testcase");

                for (int j = 0; j < testcaseNodes.getLength(); j++) {
                    Node tcNode = testcaseNodes.item(j);
                    if (tcNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Element testcase = (Element) tcNode;
                    ParsedTestCase testCase = new ParsedTestCase();
                    testCase.setClassName(finalClassName);

                    String methodName = testcase.getAttribute("methodname");
                    testCase.setMethodName(methodName);

                    JsonNode test_info_node = null;
                    for (JsonNode infoNode : testInfoRoot) {
                        if (simpleClassName.equals(infoNode.path("className").asText()) && methodName != null && methodName.equals(infoNode.path("methodName").asText())) {
                            test_info_node = infoNode;
                            break;
                        }
                    }
                    if (test_info_node == null) {
                        continue;
                    }

                    String duration = testcase.getAttribute("time");
                    testCase.setDuration(parseDoubleSafe(duration));

                    String ts = testcase.getAttribute("timestamp_execution");
                    testCase.setTimestamp_execution(ts);

                    testCase.setTestID(test_info_node.path("testId").asText(null));
                    testCase.setTestCaseFilePath(test_info_node.path("testCaseFilePath").asText(null));
                    testCase.setModuleName(test_info_node.path("moduleName").asText(null));

                    if (test_info_node.has("startLine") && !test_info_node.get("startLine").isNull()) {
                        testCase.setStartLine(test_info_node.get("startLine").asInt());
                    }
                    if (test_info_node.has("endLine") && !test_info_node.get("endLine").isNull()) {
                        testCase.setEndLine(test_info_node.get("endLine").asInt());
                    }

                    testCase.setOwnershipSource(test_info_node.path("ownershipSource").asText(null));

                    if (test_info_node.has("confidenceScore") && !test_info_node.get("confidenceScore").isNull()) {
                        testCase.setConfidenceScore(test_info_node.get("confidenceScore").asDouble());
                    }

                    testCase.setCreatedAt(test_info_node.path("createdAt").asText(null));
                    testCase.setLastModifiedAt(test_info_node.path("lastModifiedAt").asText(null));
                    testCase.setLastModifiedBy(test_info_node.path("lastModifiedBy").asText(null));
                    testCase.setCurrentCommitSha(test_info_node.path("currentCommitSha").asText(null));

                    String status = "PASSED";
                    String errorMessage = null;
                    String stackTrace = null;

                    NodeList children = testcase.getChildNodes();
                    for (int k = 0; k < children.getLength(); k++) {
                        Node child = children.item(k);
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

                    String lifecycleStatus = "ACTIVE";
                    if ("SKIPPED".equals(status)) {
                        lifecycleStatus = "INACTIVE";
                    } else {
                        String lastModStr = test_info_node.path("lastModifiedAt").asText(null);
                        if (lastModStr != null && !lastModStr.isBlank()) {
                            java.time.Instant lastModified = java.time.Instant.parse(lastModStr);
                            java.time.Instant sixMonthsAgo = java.time.Instant.now().minus(180, java.time.temporal.ChronoUnit.DAYS);

                            if (lastModified.isBefore(sixMonthsAgo)) {
                                lifecycleStatus = "INACTIVE";
                            }
                        }
                    }

                    testCase.setCurrentLifecycleStatus(lifecycleStatus);

                    parsedClass.getTestCases().add(testCase);
                }
            }

            report.setTestClasses(new ArrayList<>(classMap.values()));
            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse structural custom XML report", e);
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