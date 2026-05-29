package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.dto.request.IngestReportRequest;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestDocumentMapper {
    public TestExecutionDocument toDocuments_json(IngestReportRequest request){
        if (request == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        return toDocuments_json_setter(request);
    }

    private TestExecutionDocument toDocuments_json_setter(IngestReportRequest request) {
        TestExecutionDocument document = new TestExecutionDocument();
        document.setTestName(request.getTestName());
        document.setTestclass(request.getTestclass());
        document.setStatus(request.getStatus());
        document.setDuration(request.getDuration());
        document.setBuildID(request.getBuildID());
        document.setOwnerID(request.getOwnerID());
        document.setTimestamp(request.getTimestamp() != null ? request.getTimestamp() : Instant.now());
        document.setErrorMessage(request.getErrorMessage());
        document.setStackTrace(request.getStackTrace());
        document.setCommitID(request.getCommitID());
        document.setBranchName(request.getBranchName());
        document.setIsFlaky(false); // Default value, can be updated later based on analysis

        return document;
    }

    public List<TestExecutionDocument> toDocuments_xml(ParsedTestReport report) {
        if (report == null || report.getTestCases() == null) {
            throw new IllegalArgumentException("ParsedTestReport and its test cases cannot be null");
        }

        return report.getTestCases().stream().map(testCase -> toDocuments_xml_setter(report, testCase)).collect(Collectors.toList());
    }

    private TestExecutionDocument toDocuments_xml_setter(ParsedTestReport report, ParsedTestCase testCase) {
        TestExecutionDocument document = new TestExecutionDocument();
        document.setTestName(testCase.getTestName());
        document.setTestclass(testCase.getTestClass());
        document.setStatus(testCase.getStatus());
        document.setDuration(testCase.getDuration());
        document.setOwnerID(testCase.getOwnerID());
        document.setBuildID(report.getBuildID());
        document.setTimestamp(testCase.getTimestamp() != null ? testCase.getTimestamp() : Instant.now());
        document.setErrorMessage(testCase.getErrorMessage());
        document.setStackTrace(testCase.getStackTrace());
        document.setCommitID(report.getCommitID());
        document.setBranchName(report.getBranchName());
        document.setIsFlaky(false); // Default value, can be updated later based on analysis

        return document;
    }
}
