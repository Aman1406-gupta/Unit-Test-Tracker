package com.sprinklr.unittesttracker.util;

import java.util.List;

public class FlakyTestHeuristic {
    public boolean isFlaky(List<String> testStatus){
        boolean hasFailure = testStatus.contains("FAILED");
        boolean hasPass = testStatus.contains("PASSED");
        return hasFailure && hasPass;
    }
}
