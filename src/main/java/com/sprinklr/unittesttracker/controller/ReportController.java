package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.dto.IngestReportRequest;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.ReportIngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportIngestionService service;

    public ReportController(ReportIngestionService service) {
        this.service = service;
    }

    @PostMapping
    public TestExecutionDocument ingestReport(@Valid @RequestBody IngestReportRequest request) {
        return service.saveTestResult(request);
    }
}