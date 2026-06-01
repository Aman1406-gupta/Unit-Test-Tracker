package com.sprinklr.unittesttracker.controller;

import com.sprinklr.unittesttracker.service.XmlReportIngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/xml-reports")
public class XmlReportController {
    private final XmlReportIngestionService service;

    public XmlReportController(XmlReportIngestionService service) {
        this.service = service;
    }

    @PostMapping(value = "/xml", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadXmlReport(@RequestParam("file") MultipartFile file, @RequestParam("buildID") String buildID, @RequestParam("commitID") String commitID, @RequestParam("branchName") String branchName) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("XML file is empty");
            }
            String message = service.ingestXmlReport(file, buildID, commitID, branchName);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to read XML file" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Bad Request : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : Failed to save XML test report" + e.getMessage());
        }
    }
}
