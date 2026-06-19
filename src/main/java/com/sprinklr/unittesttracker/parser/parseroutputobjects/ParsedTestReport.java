package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.ArrayList;
import java.util.List;

public class ParsedTestReport {
    private String suiteName;
    private String buildID;
    private String repository_url;
    private String branchName;
    private String jobName;

    private List<ParsedTestClass> testClasses = new ArrayList<>();

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public List<ParsedTestClass> getTestClasses() {
        return testClasses;
    }

    public void setTestClasses(List<ParsedTestClass> testClasses) {
        this.testClasses = testClasses;
    }

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
    }

    public String getRepository_url() {
        return repository_url;
    }

    public void setRepository_url(String repository_url) {
        this.repository_url = repository_url;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}