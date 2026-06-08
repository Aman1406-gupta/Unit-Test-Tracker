package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.JsonReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/test-reports")
public class JsonReportController {
    private final JsonReportIngestionService service;

    public JsonReportController(JsonReportIngestionService service) {
        this.service = service;
    }

    @PostMapping(value = "/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ingestReportMultipart(@RequestPart("jsonContent") String jsonContent, @RequestPart(value = "metadata") MultipartFile metadataFile) {
        try {
            List<TestExecutionDocument> saved = service.saveAllTestResults(jsonContent, metadataFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to save json test report " + e.getMessage());
        }
    }
}