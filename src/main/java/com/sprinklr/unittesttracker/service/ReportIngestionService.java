package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.dto.request.IngestReportRequest;
import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportIngestionService {

    private final TestExecutionRepository repository;
    private final TestDocumentMapper mapper;

    public ReportIngestionService(TestExecutionRepository repository, TestDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public TestExecutionDocument saveTestResult(IngestReportRequest request) {
        TestExecutionDocument document = mapper.toDocuments_json(request);
        return repository.save(document);
    }

    public TestExecutionDocument getReportById(String id) {
        return repository.getReportById(id).orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
    }

    public List<TestExecutionDocument> getReportsByBuildID(String buildID) {
        List<TestExecutionDocument>documents = repository.getReportsByBuildID(buildID);
        if (documents.isEmpty()) {
            throw new RuntimeException("No reports found for buildID: " + buildID);
        }
        return documents;
    }

    public List<TestExecutionDocument> getReportsByStatus(String status) {
        List<TestExecutionDocument>documents = repository.getReportsByStatus(status);
        if (documents.isEmpty()) {
            throw new RuntimeException("No reports found with status: " + status);
        }
        return documents;
    }
}