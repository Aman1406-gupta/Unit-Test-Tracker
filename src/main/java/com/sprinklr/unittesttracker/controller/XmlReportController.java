package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.service.XmlReportIngestionService;
import com.sprinklr.unittesttracker.service.TestChangeEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test-reports")
public class XmlReportController {

    private final XmlReportIngestionService service;
    private final TestChangeEventService testChangeEventService;

    public XmlReportController(XmlReportIngestionService service, TestChangeEventService testChangeEventService) {
        this.service = service;
        this.testChangeEventService = testChangeEventService;
    }

    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadXmlReportMultipart(@RequestPart("report") MultipartFile reportFile, @RequestPart(value = "testInfo") MultipartFile testInfoFile) {
        try {
            service.ingestJunit(reportFile, testInfoFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("JUnit ingested successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error:" + e.getMessage());
        }
    }
}