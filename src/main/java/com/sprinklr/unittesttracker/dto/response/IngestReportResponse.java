package com.sprinklr.unittesttracker.dto.response;

public class IngestReportResponse {
    private String message;
    private int totalTests;

    public IngestReportResponse() {
    }

    public IngestReportResponse(String message, int totalTests) {
        this.message = message;
        this.totalTests = totalTests;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }
}
