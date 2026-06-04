package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ParsedTestReport { // One unit test is corresponding to one test suite.
    private String buildID_suite;
    private String commitID_suite;
    private String branchName_suite;
    private String suiteName;
    private Instant timestamp_suite;

    private int tests;
    private int failures;
    private int errors;
    private int skipped;

    private List<ParsedTestClass> testClasses = new ArrayList<>();

    public String getBuildID_suite() {
        return buildID_suite;
    }

    public void setBuildID_suite(String buildID_suite) {
        this.buildID_suite = buildID_suite;
    }

    public String getCommitID_suite() {
        return commitID_suite;
    }

    public void setCommitID_suite(String commitID_suite) {
        this.commitID_suite = commitID_suite;
    }

    public String getBranchName_suite() {
        return branchName_suite;
    }

    public void setBranchName_suite(String branchName_suite) {
        this.branchName_suite = branchName_suite;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public Instant getTimestamp_suite() {
        return timestamp_suite;
    }

    public void setTimestamp_suite(Instant timestamp_suite) {
        this.timestamp_suite = timestamp_suite;
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

    public List<ParsedTestClass> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(List<ParsedTestClass> testClasses) {
        this.testClasses = testClasses;
    }
}