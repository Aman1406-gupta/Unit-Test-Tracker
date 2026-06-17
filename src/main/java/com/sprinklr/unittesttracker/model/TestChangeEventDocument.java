package com.sprinklr.unittesttracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;

@Document(indexName = "test_change_event")
public class TestChangeEventDocument {
    @Field(type = FieldType.Keyword)
    @Id
    private String eventID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String buildID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String repositoryURL;
    
    @Field(type = FieldType.Keyword)
    @NotBlank
    private String branchName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private ChangeType changeType;
    
    public enum ChangeType {
        ADDED,
        MODIFIED,
        DELETED,
        UNCHANGED
    }

    @Field(type = FieldType.Keyword)
    private String previousCommitSha;

    @Field(type = FieldType.Keyword)
    @NotNull
    private String currentCommitSha;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @NotNull
    private Instant detectedAt;

    public String getEventID() {
        return eventID;
    }
    
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getTestID() {
        return testID;
    }
    
    public void setTestID(String testID) {
        this.testID = testID;
    }
    
    public String getBuildID() {
        return buildID;
    }
    
    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }
    
    public String getRepositoryURL() {
        return repositoryURL;
    }
    
    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public ChangeType getChangeType() {
        return changeType;
    }
    
    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
    
    public String getPreviousCommitSha() {
        return previousCommitSha;
    }
    
    public void setPreviousCommitSha(String previousCommitSha) {
        this.previousCommitSha = previousCommitSha;
    }
    
    public String getCurrentCommitSha() {
        return currentCommitSha;
    }

    public void setCurrentCommitSha(String currentCommitSha) {
        this.currentCommitSha = currentCommitSha;
    }
    
    public Instant getDetectedAt() {
        return detectedAt;
    }
    
    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }
}