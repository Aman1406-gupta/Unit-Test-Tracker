package com.sprinklr.unittesttracker.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "failure_insight")
public class FailureInsightDocument {
    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String owner;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer failureCount;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer consecutiveFailureCount;
    
    @Field(type = FieldType.Keyword)
    @NotBlank
    private String lastFailedBuildID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String lastFailureMessage;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String lastStackTraceSummary;

    @Field(type = FieldType.Double)
    @NotBlank
    private double recurrenceScore;

    @Field(type = FieldType.Boolean)
    @NotBlank
    private boolean flakyCandidateFlag;
    
    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getFailureCount() {
        return failureCount;
    }
    
    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Integer getConsecutiveFailureCount() {
        return consecutiveFailureCount;
    }
    
    public void setConsecutiveFailureCount(Integer consecutiveFailureCount) {
        this.consecutiveFailureCount = consecutiveFailureCount;
    }

    public String getLastFailedBuildID() {
        return lastFailedBuildID;
    }
    
    public void setLastFailedBuildID(String lastFailedBuildID) {
        this.lastFailedBuildID = lastFailedBuildID;
    }
    
    public String getLastFailureMessage() {
        return lastFailureMessage;
    }
    
    public void setLastFailureMessage(String lastFailureMessage) {
        this.lastFailureMessage = lastFailureMessage;
    }
    
    public String getLastStackTraceSummary() {
        return lastStackTraceSummary;
    }
    
    public void setLastStackTraceSummary(String lastStackTraceSummary) {
        this.lastStackTraceSummary = lastStackTraceSummary;
    }
    
    public double getRecurrenceScore() {
        return recurrenceScore;
    }
    
    public void setRecurrenceScore(double recurrenceScore) {
        this.recurrenceScore = recurrenceScore;
    }

    public boolean getFlakyCandidateFlag() {
        return flakyCandidateFlag;
    }
    
    public void setFlakyCandidateFlag(boolean flakyCandidateFlag) {
        this.flakyCandidateFlag = flakyCandidateFlag;
    }
}