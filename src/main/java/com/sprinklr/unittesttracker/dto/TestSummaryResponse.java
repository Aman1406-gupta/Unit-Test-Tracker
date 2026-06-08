package com.sprinklr.unittesttracker.dto;

public class TestSummaryResponse {
    private long total;
    private long passed;
    private long failed;
    private long skipped;

    public TestSummaryResponse(long total, long passed, long failed, long skipped) {
        this.total = total;
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
    }

    public double getSuccessRate() {
        return total == 0 ? 0 : (passed * 100.0 / total);
    }
}