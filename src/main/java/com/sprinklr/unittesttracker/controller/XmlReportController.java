package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.service.XmlReportIngestionService;
import com.sprinklr.unittesttracker.service.TestChangeEventService;
import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> uploadXmlReportMultipart(@RequestPart("report") MultipartFile reportFile, @RequestPart(value = "metadata") MultipartFile metadataFile, @RequestPart(value = "testInfo") MultipartFile testInfoFile) {
        try {
            List<Object> saved = service.ingestJunit(reportFile, metadataFile, testInfoFile);
            return ResponseEntity.status(HttpStatus.CREATED).body("JUnit ingested successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Failed to ingest JUnit " + e.getMessage());
        }
    }

    @GetMapping(value = "/welcome")
    public String welcome(){
        return "Welcome to Unit Test Tracker";
    }

    @PostMapping(value = "/test-change-event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String testChangeEvent(@RequestBody Map<String, String> body){
        String testID = body.get("testID");
        String buildID = body.get("buildID");

        TestChangeEventDocument testChangeEventDocument = testChangeEventService.trackChange(testID, buildID);
        return testChangeEventDocument.toString();
    }
}