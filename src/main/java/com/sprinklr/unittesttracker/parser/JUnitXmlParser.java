package com.sprinklr.unittesttracker.parser;

import org.springframework.stereotype.Component;

@Component
public class JUnitXmlParser {
    public ParsedTestReport parse(String reportContent) {
        ParsedTestReport report = new ParsedTestReport();
        return report;
    }
}
