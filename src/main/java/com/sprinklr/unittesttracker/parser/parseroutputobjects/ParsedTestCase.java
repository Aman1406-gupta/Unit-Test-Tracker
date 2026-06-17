package com.sprinklr.unittesttracker.parser.parseroutputobjects;

public class ParsedTestCase {
    private String testID; 
    private String testName; 
    private String className;  
    private String methodName; 
    private String status; 
    private double duration_test; 
    private String errorMessage; 
    private String stackTrace; 
    private String timestamp_execution;
    private String testCaseFilePath; 
    private String moduleName; 
    private int startLine; 
    private int endLine; 
    private String methodOwner; 
    private String resolvedOwner; 
    private String ownershipSource; 
    private String createdAt;
    private String lastModifiedAt;
    private String lastModifiedBy; 
    private String currentLifecycleStatus; 
    private String currentCommitSha;

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getTimestamp_execution() {
        return timestamp_execution;
    }

    public void setTimestamp_execution(String timestamp_execution) {
        this.timestamp_execution = timestamp_execution;
    }

    public String getTestCaseFilePath() {
        return testCaseFilePath;
    }

    public void setTestCaseFilePath(String testCaseFilePath) {
        this.testCaseFilePath = testCaseFilePath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public String getMethodOwner() {
        return methodOwner;
    }

    public void setMethodOwner(String methodOwner) {
        this.methodOwner = methodOwner;
    }

    public String getResolvedOwner() {
        return resolvedOwner;
    }

    public void setResolvedOwner(String resolvedOwner) {
        this.resolvedOwner = resolvedOwner;
    }

    public String getOwnershipSource() {
        return ownershipSource;
    }

    public void setOwnershipSource(String ownershipSource) {
        this.ownershipSource = ownershipSource;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(String lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCurrentLifecycleStatus() {
        return currentLifecycleStatus;
    }

    public void setCurrentLifecycleStatus(String currentLifecycleStatus) {
        this.currentLifecycleStatus = currentLifecycleStatus;
    }

    public String getCurrentCommitSha() {
        return currentCommitSha;
    }

    public void setCurrentCommitSha(String currentCommitSha) {
        this.currentCommitSha = currentCommitSha;
    }
}