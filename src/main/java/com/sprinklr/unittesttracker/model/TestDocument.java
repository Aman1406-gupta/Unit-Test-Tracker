package com.sprinklr.unittesttracker.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.List;

@Document(indexName = "test")
public class TestDocument {

    @Field(type = FieldType.Keyword)
    @Id
    private String testID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String buildID;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String repositoryUrl;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String branchName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String jobName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String className;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String methodName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String suiteName;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String status;

    @Field(type = FieldType.Double)
    @NotBlank
    private double duration_test;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String stackTrace;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String errorMessage;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String timestampExecution;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String testCaseFilePath;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String moduleName;

    @Field(type = FieldType.Integer)
    @NotBlank
    private int startLine;

    @Field(type = FieldType.Integer)
    @NotBlank
    private int endLine;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private List<String> owners;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String ownershipSource;

    @Field(type = FieldType.Double)
    @NotBlank
    private Double confidenceScore;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String createdAt;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String lastModifiedAt;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String lastModifiedBy;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String currentLifecycleStatus;

    @Field(type = FieldType.Keyword)
    @NotBlank
    private String currentCommitSha;

    public String getTestID(){
        return testID;
    }

    public void setTestID(String testID){
        this.testID = testID;
    }
    
    public String getBuildID(){
        return buildID;
    }

    public void setBuildID(String buildID){
        this.buildID = buildID;
    }

    public String getRepositoryUrl(){
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl){
        this.repositoryUrl = repositoryUrl;
    }

    public String getBranchName(){
        return branchName;
    }

    public void setBranchName(String branchName){
        this.branchName = branchName;
    }
    
    public String getJobName(){
        return jobName;
    }

    public void setJobName(String jobName){
        this.jobName = jobName;
    }
    
    public String getClassName(){
        return className;
    }

    public void setClassName(String className){
        this.className = className;
    }
    
    public String getMethodName(){
        return methodName;
    }

    public void setMethodName(String methodName){
        this.methodName = methodName;
    }

    public String getSuiteName(){
        return suiteName;
    }

    public void setSuiteName(String suiteName){
        this.suiteName = suiteName;
    }
    
    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public double getDuration(){
        return duration_test;
    }

    public void setDuration(double duration_test){
        this.duration_test = duration_test;
    }

    public String getStackTrace(){
        return stackTrace;
    }

    public void setStackTrace(String stackTrace){
        this.stackTrace = stackTrace;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getTimestampExecution(){
        return timestampExecution;
    }

    public void setTimestampExecution(String timestampExecution){
        this.timestampExecution = timestampExecution;
    }

    public String getTestCaseFilePath(){
        return testCaseFilePath;
    }

    public void setTestCaseFilePath(String testCaseFilePath){
        this.testCaseFilePath = testCaseFilePath;
    }
    
    public String getModuleName(){
        return moduleName;
    }
    
    public void setModuleName(String moduleName){
        this.moduleName = moduleName;
    }

    public int getStartLine(){
        return startLine;
    }

    public void setStartLine(int startLine){
        this.startLine = startLine;
    }

    public int getEndLine(){
        return endLine;
    }

    public void setEndLine(int endLine){
        this.endLine = endLine;
    }
    
    public List<String> getOwners(){
        return owners;
    }

    public void setOwners(List<String> owners){
        this.owners = owners;
    }

    public String getOwnershipSource(){
        return ownershipSource;
    }

    public void setOwnershipSource(String ownershipSource){
        this.ownershipSource = ownershipSource;
    }

    public Double getConfidenceScore(){
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore){
        this.confidenceScore = confidenceScore;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getLastModifiedAt(){
        return lastModifiedAt;
    }

    public void setLastModifiedAt(String lastModifiedAt){
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getLastModifiedBy(){
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy){
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCurrentLifecycleStatus(){
        return currentLifecycleStatus;
    }

    public void setCurrentLifecycleStatus(String currentLifecycleStatus){
        this.currentLifecycleStatus = currentLifecycleStatus;
    }

    public String getCurrentCommitSha(){
        return currentCommitSha;
    }

    public void setCurrentCommitSha(String currentCommitSha){
        this.currentCommitSha = currentCommitSha;
    }
}
