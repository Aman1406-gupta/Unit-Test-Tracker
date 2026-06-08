package com.sprinklr.unittesttracker.parser.parseroutputobjects;

import java.util.ArrayList;
import java.util.List;

public class ParsedTestClass {
    private String className;
    private List<ParsedTestCase> testCases = new ArrayList<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ParsedTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<ParsedTestCase> testCases) {
        this.testCases = testCases;
    }
}