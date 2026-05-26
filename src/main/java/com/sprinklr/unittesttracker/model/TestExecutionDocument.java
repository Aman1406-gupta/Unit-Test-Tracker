package com.sprinklr.unittesttracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Document(indexName = "test_execution")
public class TestExecutionDocument {
    @Id
    private String id;
    @NotBlank
    private String testName;
    @NotBlank
    private String status;
    @PositiveOrZero
    private double duration;
    @NotBlank
    private String buildId;

    public TestExecutionDocument() {
    }

    public TestExecutionDocument(String id, String testName, String status, double duration, String buildId) {
        this.id = id;
        this.testName = testName;
        this.status = status;
        this.duration = duration;
        this.buildId = buildId;
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

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }
}
