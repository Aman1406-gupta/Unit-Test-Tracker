package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.time.Instant;

public class ParsedTestCase {
    private String testName;
    private String methodName;
    private String status;
    private double duration_test;
    private String errorMessage;
    private String stackTrace;
    private Instant timestamp_execution;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDuration() {
        return duration_test;
    }

    public void setDuration(double duration_test) {
        this.duration_test = duration_test;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Instant getTimestamp_execution() {
        return timestamp_execution;
    }

    public void setTimestamp_execution(Instant timestamp_execution) {
        this.timestamp_execution = timestamp_execution;
    }
}