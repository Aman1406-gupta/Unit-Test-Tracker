package com.sprinklr.unittesttracker.model;

import com.sprinklr.unittesttracker.parser.parseroutputobjects.Metadata;
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

    @Field(type = FieldType.Object)
    @NotNull
    private Metadata metadata;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String className;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String suiteName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String methodName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String status;

    @Field(type = FieldType.Double)
    @PositiveOrZero
    private double duration_test;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @NotNull
    private Instant timestamp_execution;

    @Field(type = FieldType.Text)
    @NotBlank
    private String errorMessage;

    @Field(type = FieldType.Text)
    @NotBlank
    private String stackTrace;

    @Field(type = FieldType.Boolean)
    private boolean isFlaky;

    @Field(type = FieldType.Integer)
    @PositiveOrZero
    public int suiteTotalTests;

    @Field(type = FieldType.Integer)
    @PositiveOrZero
    public int suiteTotalFailures;

    @Field(type = FieldType.Integer)
    @PositiveOrZero
    public int suiteTotalErrors;

    @Field(type = FieldType.Integer)
    @PositiveOrZero
    public int suiteTotalSkipped;

    public TestExecutionDocument() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
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

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
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

    public Instant getTimestamp_execution() {
        return timestamp_execution;
    }

    public void setTimestamp_execution(Instant timestamp_execution) {
        this.timestamp_execution = timestamp_execution;
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

    public boolean getIsFlaky() {
        return isFlaky;
    }

    public void setIsFlaky(boolean isFlaky) {
        this.isFlaky = isFlaky;
    }

    public void setSuiteTotalTests(int totalTests) {
        this.suiteTotalTests = totalTests;
    }

    public void setSuiteTotalFailures(int totalFailures) {
        this.suiteTotalFailures = totalFailures;
    }

    public void setSuiteTotalErrors(int totalErrors) {
        this.suiteTotalErrors = totalErrors;
    }

    public void setSuiteTotalSkipped(int totalSkipped) {
        this.suiteTotalSkipped = totalSkipped;
    }
}