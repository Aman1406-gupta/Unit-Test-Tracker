package com.sprinklr.unittesttracker.parser.parseroutputobjects;

public class ParsedBuildMetadata {
    private String repositoryUrl;
    private String branchName;
    private String buildID;
    private String jobName;
    private String buildUrl;
    private String commitSha;
    private String suiteName;
    private String timestamp_generation;
    private Integer totalFailure;
    private Integer totalTests;
    private Integer totalSkipped;
    private Integer totalPassed;

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

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
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

    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getTimestamp_generation() {
        return timestamp_generation;
    }

    public void setTimestamp_generation(String timestamp_generation) {
        this.timestamp_generation = timestamp_generation;
    }

    public Integer getTotalFailure() {
        return totalFailure;
    }

    public void setTotalFailure(Integer totalFailure) {
        this.totalFailure = totalFailure;
    }
    
    public Integer getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
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
