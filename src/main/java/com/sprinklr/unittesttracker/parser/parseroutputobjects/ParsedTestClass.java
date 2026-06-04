package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.ArrayList;
import java.util.List;

public class ParsedTestClass {
    private String className;
    private int tests;
    private int failures;
    private int errors;
    private int skipped;
    private double duration;
    private List<ParsedTestCase> testCases = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<ParsedTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<ParsedTestCase> testCases) {
        this.testCases = testCases;
    }
}