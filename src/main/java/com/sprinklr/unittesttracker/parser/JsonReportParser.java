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

            report.setMetadata(null);
            report.setSuiteName(text(root, "suiteName"));

            List<ParsedTestClass> classes = new ArrayList<>();

            JsonNode testClassesNode = root.path("testClasses");

            if (testClassesNode.isArray()) {
                for (JsonNode classNode : testClassesNode) {
                    ParsedTestClass testClass = new ParsedTestClass();

                    testClass.setClassName(text(classNode, "className"));
                    testClass.setDuration(dbl(classNode, "duration"));

                    List<ParsedTestCase> cases = new ArrayList<>();

                    JsonNode testCasesNode = classNode.path("testCases");

                    if (testCasesNode.isArray()) {
                        for (JsonNode caseNode : testCasesNode) {
                            ParsedTestCase testCase = new ParsedTestCase();
                            testCase.setTestName(text(caseNode, "testName"));
                            String methodName = text(caseNode, "method");

                            if (methodName == null || methodName.isBlank()) {
                                methodName = text(caseNode, "methodName");
                            }

                            testCase.setMethodName(methodName);
                            testCase.setStatus(defaultText(text(caseNode, "status"), "PASSED"));
                            testCase.setDuration(dbl(caseNode, "duration"));
                            testCase.setErrorMessage(text(caseNode, "errorMessage"));
                            testCase.setStackTrace(text(caseNode, "stackTrace"));
                            testCase.setTimestamp_execution(instant(caseNode, "timestamp_execution"));
                            cases.add(testCase);
                        }
                    }

                    testClass.setTestCases(cases);
                    recalculateClassSummary(testClass, classNode);
                    classes.add(testClass);
                }
            }

            report.setTestClasses(classes);
            recalculateSuiteSummary(report, root);

            return report;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON test report", e);
        }
    }

    private void recalculateClassSummary(ParsedTestClass testClass, JsonNode classNode) {
        int tests = testClass.getTestCases().size();
        int failures = 0;
        int errors = 0;
        int skipped = 0;

        for (ParsedTestCase testCase : testClass.getTestCases()) {
            if ("FAILED".equalsIgnoreCase(testCase.getStatus())) {
                failures++;
            }
            else if ("ERROR".equalsIgnoreCase(testCase.getStatus())) {
                errors++;
            }
            else if ("SKIPPED".equalsIgnoreCase(testCase.getStatus())) {
                skipped++;
            }
        }

        int declaredTests = integer(classNode, "tests");
        int declaredFailures = integer(classNode, "failures");
        int declaredErrors = integer(classNode, "errors");
        int declaredSkipped = integer(classNode, "skipped");

        testClass.setTests(declaredTests == tests || declaredTests == 0 ? tests : declaredTests);
        testClass.setFailures(declaredFailures == failures || declaredFailures == 0 ? failures : declaredFailures);
        testClass.setErrors(declaredErrors == errors || declaredErrors == 0 ? errors : declaredErrors);
        testClass.setSkipped(declaredSkipped == skipped || declaredSkipped == 0 ? skipped : declaredSkipped);
    }

    private void recalculateSuiteSummary(ParsedTestReport report, JsonNode root) {
        int totalTests = 0;
        int totalFailures = 0;
        int totalErrors = 0;
        int totalSkipped = 0;

        for (ParsedTestClass testClass : report.getTestClasses()) {
            totalTests += testClass.getTests();
            totalFailures += testClass.getFailures();
            totalErrors += testClass.getErrors();
            totalSkipped += testClass.getSkipped();
        }

        int declaredTests = integer(root, "totalTests");
        int declaredFailures = integer(root, "totalFailures");
        int declaredErrors = integer(root, "totalErrors");
        int declaredSkipped = integer(root, "totalSkipped");

        report.setTotalTests(declaredTests == totalTests || declaredTests == 0 ? totalTests : declaredTests);
        report.setTotalFailures(declaredFailures == totalFailures || declaredFailures == 0 ? totalFailures : declaredFailures);
        report.setTotalErrors(declaredErrors == totalErrors || declaredErrors == 0 ? totalErrors : declaredErrors);
        report.setTotalSkipped(declaredSkipped == totalSkipped || declaredSkipped == 0 ? totalSkipped : declaredSkipped);
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