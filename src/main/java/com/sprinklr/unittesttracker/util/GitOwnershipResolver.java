package com.sprinklr.unittesttracker.util;

import org.springframework.stereotype.Component;

@Component
public class GitOwnershipResolver {
    public String resolveOwner(String filePath) {
        return "Unknown_owner";
    }
}
