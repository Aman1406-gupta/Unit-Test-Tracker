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
            document.setTestName(testCase.getTestName());
            document.setTestClass(testClass.getClassName());
            document.setSuiteName(report.getSuiteName());
            document.setStatus(testCase.getStatus());
            document.setDuration(testCase.getDuration());
            document.setBuildID(report.getBuildID_suite());
            document.setTimestamp(testCase.getTimestamp() != null ? testCase.getTimestamp() : Instant.now());
            document.setErrorMessage(testCase.getErrorMessage());
            document.setStackTrace(testCase.getStackTrace());
            document.setCommitID(report.getCommitID_suite());
            document.setBranchName(report.getBranchName_suite());
            document.setIsFlaky(false);
            return document;
        }).collect(Collectors.toList());
    }
}