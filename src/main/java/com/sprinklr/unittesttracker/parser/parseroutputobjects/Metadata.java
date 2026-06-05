package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.time.Instant;

public class Metadata {
    private String repositoryUrl;
    private String branchName;
    private String commitID;
    private String buildID;
    private String jobName;
    private String buildUrl;
    private String testReportPath;
    private Instant timestamp_generation;

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

    public String getCommitID() {
        return commitID;
    }

    public void setCommitID(String commitID) {
        this.commitID = commitID;
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

    public String getTestReportPath() {
        return testReportPath;
    }

    public void setTestReportPath(String testReportPath) {
        this.testReportPath = testReportPath;
    }

    public Instant getTimestamp_generation() {
        return timestamp_generation;
    }

    public void setTimestamp_generation(Instant timestamp_generation) {
        this.timestamp_generation = timestamp_generation;
    }

}
