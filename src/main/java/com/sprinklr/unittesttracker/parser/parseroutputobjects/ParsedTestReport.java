package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.ArrayList;
import java.util.List;

public class ParsedTestReport {
    private Metadata metadata;
    private String suiteName;
    private int totalTests;
    private int totalFailures;
    private int totalErrors;
    private int totalSkipped;

    private List<ParsedTestClass> testClasses = new ArrayList<>();

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public List<ParsedTestClass> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(List<ParsedTestClass> testClasses) {
        this.testClasses = testClasses;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public void setTotalFailures(int totalFailures) {
        this.totalFailures = totalFailures;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    public void setTotalSkipped(int totalSkipped) {
        this.totalSkipped = totalSkipped;
    }

}