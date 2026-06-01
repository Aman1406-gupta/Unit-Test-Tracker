package com.sprinklr.unittesttracker.parser;

import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class JUnitXmlParser {
    public ParsedTestReport parse(String xmlContent, String buildID, String commitID, String branchName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setExpandEntityReferences(false);
            Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();

            ParsedTestReport report = new ParsedTestReport();
            report.setBuildID(buildID);
            report.setCommitID(commitID);
            report.setBranchName(branchName);

            List<ParsedTestCase> testCases = new ArrayList<>();

            NodeList testcaseNodes = doc.getElementsByTagName("testcase");
            for (int i = 0; i < testcaseNodes.getLength(); i++) {
                Node node = testcaseNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element testcase = (Element) node;
                ParsedTestCase testCase = new ParsedTestCase();
                testCase.setTestName(testcase.getAttribute("name"));
                testCase.setTestClass(testcase.getAttribute("classname"));
                testCase.setDuration(Double.parseDouble(testcase.getAttribute("time")));
                testCase.setTimestamp(Instant.now());

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
                testCases.add(testCase);
            }
            report.setTestCases(testCases);
            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JUnit XML report", e);
        }
    }
}
