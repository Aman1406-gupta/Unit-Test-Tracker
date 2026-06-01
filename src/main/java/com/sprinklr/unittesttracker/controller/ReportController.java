package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.dto.request.IngestReportRequest;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.ReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

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
        try {
            TestExecutionDocument saved = service.saveTestResult(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to save json test report" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
        try {
            TestExecutionDocument document = service.getReportById(id);
            if (document != null) {
                return ResponseEntity.ok(document);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Report not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to retrieve report" + e.getMessage());
        }
    }

    @GetMapping("/build/{buildID}")
    public ResponseEntity<?> getReportsByBuildID(@PathVariable String buildID) {
        try {
            List<TestExecutionDocument> documents = service.getReportsByBuildID(buildID);
            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found for buildID: " + buildID);
            } else {
                return ResponseEntity.ok(documents);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to retrieve reports for buildID: " + buildID + ". Error: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getReportsByStatus(@PathVariable String status) {
        try {
            List<TestExecutionDocument> documents = service.getReportsByStatus(status);
            if (documents.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found with status: " + status);
            } else {
                return ResponseEntity.ok(documents);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to retrieve reports for status: " + status + ". Error: " + e.getMessage());
        }
    }
}