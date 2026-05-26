package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FailureAnalysisServiceTest {
    private final FaliureAnalysisService service = new FaliureAnalysisService();

    @Test
    void shouldReturnWhenTestFails() {
        TestExecutionDocument document = new TestExecutionDocument();
        document.setStatus("FAILED");

        boolean result = service.isTestFailed(document);

        Assertions.assertTrue(result);
    }
}