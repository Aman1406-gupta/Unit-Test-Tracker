package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.dto.request.IngestReportRequest;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.ReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.awt.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportIngestionService service;

    public ReportController(ReportIngestionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> ingestReport(@Valid @RequestBody IngestReportRequest request) {
        try{
            TestExecutionDocument saved = service.saveTestResult(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to save json test report" + e.getMessage());
        }
    }
}