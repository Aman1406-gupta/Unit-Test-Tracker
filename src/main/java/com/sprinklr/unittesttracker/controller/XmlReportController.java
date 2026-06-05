package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.service.XmlReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/test-reports")
public class XmlReportController {
    private final XmlReportIngestionService service;

    public XmlReportController(XmlReportIngestionService service) {
        this.service = service;
    }

    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadXmlReportMultipart(@RequestPart("report") MultipartFile reportFile, @RequestPart(value = "metadata") MultipartFile metadataFile) {
        try {
            List<TestExecutionDocument> saved = service.ingestXmlReport_Multipart(reportFile, metadataFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Failed to save XML test report " + e.getMessage());
        }
    }
}