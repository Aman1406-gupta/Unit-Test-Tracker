package com.sprinklr.unittesttracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "build_metadata")
public class BuildMetadataDocument {

    @Field(type = FieldType.Keyword)
    @Id
    private String ID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String buildID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String suiteName;
    
    @Field(type = FieldType.Keyword)
    @NotBlank
    private String repositoryUrl;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String branchName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String commitSha;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String jobName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String buildUrl;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @NotNull
    private String timestamp_generation;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer totalTests;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer totalFailure;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer totalSkipped;

    @Field(type = FieldType.Integer)
    @NotBlank
    private Integer totalPassed;

    public String getID(){
        return ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }
    
    public String getBuildID(){
        return buildID;
    }

    public void setBuildID(String buildID){
        this.buildID = buildID;
    }

    public String getSuiteName(){
        return suiteName;
    }

    public void setSuiteName(String suiteName){
        this.suiteName = suiteName;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public void setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
    }

    public String getTimestamp_generation() {
        return timestamp_generation;
    }

    public void setTimestamp_generation(String timestamp_generation) {
        this.timestamp_generation = timestamp_generation;
    }

    public Integer getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
    }

    public Integer getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(Integer totalFailure) {
        this.totalFailure = totalFailure;
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