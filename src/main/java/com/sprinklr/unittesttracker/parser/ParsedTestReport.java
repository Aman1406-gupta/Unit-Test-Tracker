package com.sprinklr.unittesttracker.parser;

import java.util.List;

public class ParsedTestReport {
    private List<String> passedTests;
    private List<String> failedTests;

    public List<String> getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(List<String> passedTests) {
        this.passedTests = passedTests;
    }

    public List<String> getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(List<String> failedTests) {
        this.failedTests = failedTests;
    }
}
