package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.service.XmlReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xml-reports")
public class XmlReportController {
    private final XmlReportIngestionService service;

    public XmlReportController(XmlReportIngestionService service) {
        this.service = service;
    }

    @PostMapping(value = "/xml", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> uploadXmlReport(@RequestBody String xmlContent) {
        try {
            if (xmlContent == null || xmlContent.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("XML content is empty");
            }
            String message = service.ingestXmlReport(xmlContent);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Failed to save XML test report " + e.getMessage());
        }
    }
}