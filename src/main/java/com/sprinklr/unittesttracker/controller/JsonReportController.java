package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.JsonReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/json-reports")
public class JsonReportController {
    private final JsonReportIngestionService service;

    public JsonReportController(JsonReportIngestionService service) {
        this.service = service;
    }

    @PostMapping(value = "json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ingestReport(@RequestBody String jsonContent) {
        try {
            List<TestExecutionDocument> saved = service.saveAllTestResults(jsonContent);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error : Failed to save json test report " + e.getMessage());
        }
    }

    @GetMapping("/test-name/{testName}")
    public ResponseEntity<?> getReportsByTestName(@PathVariable String testName) {
        List<TestExecutionDocument> docs = service.getReportsByTestName(testName);
        return docs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found for testName: " + testName) : ResponseEntity.ok(docs);
    }

    @GetMapping("/test-class/{testClass}")
    public ResponseEntity<?> getReportsByTestClass(@PathVariable String testClass) {
        List<TestExecutionDocument> docs = service.getReportsByTestClass(testClass);
        return docs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found for testclass: " + testClass) : ResponseEntity.ok(docs);
    }

    @GetMapping("/suite/{suiteName}")
    public ResponseEntity<?> getReportsBySuiteName(@PathVariable String suiteName) {
        List<TestExecutionDocument> docs = service.getReportsBySuiteName(suiteName);
        return docs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found for suiteName: " + suiteName) : ResponseEntity.ok(docs);
    }

    @GetMapping("/build/{buildID}")
    public ResponseEntity<?> getReportsByBuildID(@PathVariable String buildID) {
        List<TestExecutionDocument> docs = service.getReportsByBuildID(buildID);
        return docs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found for buildID: " + buildID) : ResponseEntity.ok(docs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getReportsByStatus(@PathVariable String status) {
        List<TestExecutionDocument> docs = service.getReportsByStatus(status);
        return docs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reports found with status: " + status) : ResponseEntity.ok(docs);
    }
}