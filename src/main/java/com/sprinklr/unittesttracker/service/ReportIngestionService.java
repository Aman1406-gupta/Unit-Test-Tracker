package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.dto.request.IngestReportRequest;
import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;

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
}