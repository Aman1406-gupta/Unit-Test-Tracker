package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.MetadataParser;
import com.sprinklr.unittesttracker.parser.JsonReportParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    public List<TestExecutionDocument> saveAllTestResults(String jsonContent, MultipartFile metadataFile) {
        ParsedTestReport parsedReport = parser.parse(jsonContent);
        if (metadataFile != null && !metadataFile.isEmpty()) {
            try {
                MetadataParser.parse_metadata(parsedReport, metadataFile);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to read metadata file: " + e.getMessage());
            }
        }

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

    public List<TestExecutionDocument> getReportsByTestClass(String className) {
        return repository.findByClassName(className);
    }

    public List<TestExecutionDocument> getReportsBySuiteName(String suiteName) {
        return repository.findBySuiteName(suiteName);
    }

    public List<TestExecutionDocument> getReportsByBuildID(String buildID) {
        return repository.findByMetadataBuildID(buildID);
    }

    public List<TestExecutionDocument> getReportsByStatus(String status) {
        return repository.findByStatus(status);
    }
}