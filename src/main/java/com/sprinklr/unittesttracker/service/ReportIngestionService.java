package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.dto.IngestReportRequest;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportIngestionService {

    private final TestExecutionRepository repository;

    public ReportIngestionService(TestExecutionRepository repository) {
        this.repository = repository;
    }

    public TestExecutionDocument saveTestResult(IngestReportRequest request) {
        TestExecutionDocument document = new TestExecutionDocument();
        document.setTestName(request.getTestName());
        document.setStatus(request.getStatus());
        document.setDuration(request.getDuration());
        document.setBuildId(request.getBuildId());

        return repository.save(document);
    }
}