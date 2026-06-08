package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestDocumentMapper {

    public List<TestExecutionDocument> toDocuments(ParsedTestReport report) {
        if (report == null || report.getTestClasses() == null) {
            throw new IllegalArgumentException("ParsedTestReport and its testClasses cannot be null");
        }

        return report.getTestClasses().stream().flatMap(testClass -> toDocuments_setter(report, testClass).stream()).collect(Collectors.toList());
    }

    private List<TestExecutionDocument> toDocuments_setter(ParsedTestReport report, ParsedTestClass testClass) {
        return testClass.getTestCases().stream().map(testCase -> {
            TestExecutionDocument document = new TestExecutionDocument();
            document.setMetadata(report.getMetadata());
            document.setTestName(testCase.getTestName());
            document.setClassName(testClass.getClassName());
            document.setSuiteName(report.getSuiteName());
            document.setMethodName(testCase.getMethodName());
            document.setStatus(testCase.getStatus());
            document.setDuration(testCase.getDuration());
            document.setTimestamp_execution(testCase.getTimestamp_execution() != null ? testCase.getTimestamp_execution() : Instant.now());
            document.setErrorMessage(testCase.getErrorMessage());
            document.setStackTrace(testCase.getStackTrace());
            document.setIsFlaky(false);

            document.setSuiteTotalTests(report.getTotalTests());
            document.setSuiteTotalFailures(report.getTotalFailures());
            document.setSuiteTotalErrors(report.getTotalErrors());
            document.setSuiteTotalSkipped(report.getTotalSkipped());

            return document;
        }).collect(Collectors.toList());
    }
}