package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMapper {

    public List<TestDocument> toTestDocuments(ParsedTestReport parsedTestReport) {
        List<TestDocument> testDocuments = new ArrayList<>();

        for (ParsedTestClass parsedTestClass : parsedTestReport.getTestClasses()) {
            for (ParsedTestCase parsedTestCase : parsedTestClass.getTestCases()) {
                TestDocument document = new TestDocument();

                document.setTestID(parsedTestCase.getTestID());
                document.setBuildID(parsedTestReport.getBuildID());
                document.setRepositoryUrl(parsedTestReport.getRepository_url());
                document.setBranchName(parsedTestReport.getBranchName());
                document.setJobName(parsedTestReport.getJobName());
                document.setClassName(parsedTestCase.getClassName());
                document.setMethodName(parsedTestCase.getMethodName());
                document.setSuiteName(parsedTestReport.getSuiteName());
                document.setStatus(parsedTestCase.getStatus());
                document.setDuration(parsedTestCase.getDuration());
                document.setStackTrace(parsedTestCase.getStackTrace());
                document.setErrorMessage(parsedTestCase.getErrorMessage());
                document.setTimestampExecution(parsedTestCase.getTimestamp_execution());
                document.setTestCaseFilePath(parsedTestCase.getTestCaseFilePath());
                document.setModuleName(parsedTestCase.getModuleName());
                document.setStartLine(parsedTestCase.getStartLine());
                document.setEndLine(parsedTestCase.getEndLine());
                document.setOwnershipSource(parsedTestCase.getOwnershipSource());
                document.setConfidenceScore(parsedTestCase.getConfidenceScore());
                document.setCreatedAt(parsedTestCase.getCreatedAt());
                document.setLastModifiedAt(parsedTestCase.getLastModifiedAt());
                document.setLastModifiedBy(parsedTestCase.getLastModifiedBy());
                document.setCurrentCommitSha(parsedTestCase.getCurrentCommitSha());

                testDocuments.add(document);
            }
        }
        return testDocuments;
    }
}