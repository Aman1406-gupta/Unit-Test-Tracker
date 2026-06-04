package com.sprinklr.unittesttracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;

@Document(indexName = "test_execution")
public class TestExecutionDocument {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testClass;

    @Field(type = FieldType.Keyword)
    private String suiteName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String status;

    @Field(type = FieldType.Double)
    @PositiveOrZero
    private double duration;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String buildID;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @NotNull
    private Instant timestamp;

    @Field(type = FieldType.Text)
    private String errorMessage;

    @Field(type = FieldType.Text)
    private String stackTrace;

    @Field(type = FieldType.Keyword)
    private String commitID;

    @Field(type = FieldType.Keyword)
    private String branchName;

    @Field(type = FieldType.Boolean)
    private boolean isFlaky;

    public TestExecutionDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
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

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public boolean getIsFlaky() {
        return isFlaky;
    }

    public void setIsFlaky(boolean isFlaky) {
        this.isFlaky = isFlaky;
    }
}