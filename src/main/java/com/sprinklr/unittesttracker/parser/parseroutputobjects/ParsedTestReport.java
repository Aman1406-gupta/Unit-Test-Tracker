package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.ArrayList;
import java.util.List;

public class ParsedTestReport {
    private String suiteName;
    private Integer totalTests;
    private Integer totalFailures;
    private Integer totalSkipped;
    private Integer totalPassed;

    private List<ParsedTestClass> testClasses = new ArrayList<>();

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

    public Integer getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
    }

    public Integer getTotalFailures() {
        return totalFailures;
    }

    public void setTotalFailures(Integer totalFailures) {
        this.totalFailures = totalFailures;
    }

    public Integer getTotalSkipped() {
        return totalSkipped;
    }
    
    public void setTotalSkipped(Integer totalSkipped) {
        this.totalSkipped = totalSkipped;
    }

    public Integer getTotalPassed() {
        return totalPassed;
    }

    public void setTotalPassed(Integer totalPassed) {
        this.totalPassed = totalPassed;
    }
}