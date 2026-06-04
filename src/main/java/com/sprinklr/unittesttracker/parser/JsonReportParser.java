package com.sprinklr.unittesttracker.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonReportParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ParsedTestReport parse(String jsonContent) {
        try {
            JsonNode root = objectMapper.readTree(jsonContent);

            ParsedTestReport report = new ParsedTestReport();
            report.setBuildID_suite(text(root, "buildID_suite"));
            report.setCommitID_suite(text(root, "commitID_suite"));
            report.setBranchName_suite(text(root, "branchName_suite"));
            report.setSuiteName(text(root, "suiteName"));
            report.setTimestamp_suite(instant(root, "timestamp_suite"));
            report.setTests(integer(root, "tests"));
            report.setFailures(integer(root, "failures"));
            report.setErrors(integer(root, "errors"));
            report.setSkipped(integer(root, "skipped"));

            List<ParsedTestClass> classes = new ArrayList<>();
            JsonNode testClassesNode = root.path("testClasses");
            if (testClassesNode.isArray()) {
                for (JsonNode classNode : testClassesNode) {
                    ParsedTestClass testClass = new ParsedTestClass();
                    testClass.setClassName(text(classNode, "className"));
                    testClass.setTests(integer(classNode, "tests"));
                    testClass.setFailures(integer(classNode, "failures"));
                    testClass.setErrors(integer(classNode, "errors"));
                    testClass.setSkipped(integer(classNode, "skipped"));
                    testClass.setDuration(dbl(classNode, "duration"));

                    List<ParsedTestCase> cases = new ArrayList<>();
                    JsonNode testCasesNode = classNode.path("testCases");
                    if (testCasesNode.isArray()) {
                        for (JsonNode caseNode : testCasesNode) {
                            ParsedTestCase testCase = new ParsedTestCase();
                            testCase.setTestName(text(caseNode, "testName"));
                            testCase.setTestClass(nonBlank(text(caseNode, "testClass")) ? text(caseNode, "testClass") : testClass.getClassName());
                            testCase.setStatus(defaultText(text(caseNode, "status"), "PASSED"));
                            testCase.setDuration(dbl(caseNode, "duration"));
                            testCase.setErrorMessage(text(caseNode, "errorMessage"));
                            testCase.setStackTrace(text(caseNode, "stackTrace"));
                            testCase.setTimestamp(instant(caseNode, "timestamp"));

                            cases.add(testCase);
                        }
                    }

                    testClass.setTestCases(cases);
                    classes.add(testClass);
                }
            }

            report.setTestClasses(classes);
            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON test report", e);
        }
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value == null || value.isNull()) ? null : value.asText();
    }

    private String defaultText(String value, String defaultValue) {
        return nonBlank(value) ? value : defaultValue;
    }

    private boolean nonBlank(String value) {
        return value != null && !value.isBlank();
    }

    private int integer(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value == null || value.isNull()) ? 0 : value.asInt(0);
    }

    private double dbl(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value == null || value.isNull()) ? 0.0 : value.asDouble(0.0);
    }

    private Instant instant(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull() || value.asText().isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value.asText());
        } catch (Exception e) {
            return null;
        }
    }
}