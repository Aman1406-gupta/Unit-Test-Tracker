package com.sprinklr.unittesttracker.service;

import org.springframework.stereotype.Service;

@Service
public class TestDiffService {
    public boolean isNewTest(String currentTestName, String previousTestName) {
        return !currentTestName.equals(previousTestName);
    }
}
