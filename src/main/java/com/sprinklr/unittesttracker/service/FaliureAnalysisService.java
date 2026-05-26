package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import org.springframework.stereotype.Service;

@Service
public class FaliureAnalysisService {
    public boolean isTestFailed(TestExecutionDocument document) {
        return "failed".equalsIgnoreCase(document.getStatus());
    }
}
