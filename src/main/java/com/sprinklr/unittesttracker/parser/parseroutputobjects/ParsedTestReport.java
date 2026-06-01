package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.List;

public class ParsedTestReport {
    private String buildID;
    private String commitID;
    private String branchName;
    private List<ParsedTestCase> testCases;

    public String getBuildID() {
        return buildID;
    }

    public void setBuildID(String buildID) {
        this.buildID = buildID;
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

    public List<ParsedTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<ParsedTestCase> testCases) {
        this.testCases = testCases;
    }
}
