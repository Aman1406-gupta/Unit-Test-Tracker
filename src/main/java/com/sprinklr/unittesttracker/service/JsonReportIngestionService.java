package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.JsonReportParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JsonReportIngestionService {

    private final TestExecutionRepository repository;
    private final TestDocumentMapper mapper;
    private final JsonReportParser parser;

    public JsonReportIngestionService(TestExecutionRepository repository, TestDocumentMapper mapper, JsonReportParser parser) {
        this.repository = repository;
        this.mapper = mapper;
        this.parser = parser;
    }

    public TestExecutionDocument saveALLTestResults(String jsonContent) {
        ParsedTestReport parsedReport = parser.parse(jsonContent);
        List<TestExecutionDocument> documents = mapper.toDocuments(parsedReport);

        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("No test cases found in JSON report");
        }

        return repository.save(documents.get(0));
    }

    public List<TestExecutionDocument> saveAllTestResults(String jsonContent) {
        ParsedTestReport parsedReport = parser.parse(jsonContent);
        List<TestExecutionDocument> documents = mapper.toDocuments(parsedReport);

        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("No test cases found in JSON report");
        }

        repository.saveAll(documents);
        return documents;
    }

    public List<TestExecutionDocument> getReportsByTestName(String testName) {
        return repository.findByTestName(testName);
    }

    public List<TestExecutionDocument> getReportsByTestClass(String testClass) {
        return repository.findByTestClass(testClass);
    }

    public List<TestExecutionDocument> getReportsBySuiteName(String suiteName) {
        return repository.findBySuiteName(suiteName);
    }

    public List<TestExecutionDocument> getReportsByBuildID(String buildID) {
        return repository.findByBuildID(buildID);
    }

    public List<TestExecutionDocument> getReportsByStatus(String status) {
        return repository.findByStatus(status);
    }
}