package com.sprinklr.unittesttracker.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedBuildMetadata;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;

@Component
public class BuildMetadataParser {

    public static ParsedBuildMetadata parseBuildMetadata(MultipartFile metadataFile, ParsedTestReport parsedReport) {
        try {
            String content = new String(metadataFile.getBytes(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(content);

            ParsedBuildMetadata parsedBuildMetadata = new ParsedBuildMetadata();

            for (ParsedTestClass testClass : parsedReport.getTestClasses()) {
                for (ParsedTestCase testCase : testClass.getTestCases()) {

                    parsedBuildMetadata.setCommitSha(testCase.getCurrentCommitSha());
                    parsedBuildMetadata.setSuiteName(parsedReport.getSuiteName());
                    parsedBuildMetadata.setRepositoryUrl(text(root, "repositoryUrl"));
                    parsedBuildMetadata.setBranchName(text(root, "branchName"));
                    parsedBuildMetadata.setBuildID(text(root, "buildID"));
                    parsedBuildMetadata.setBuildUrl(text(root, "buildUrl"));
                    parsedBuildMetadata.setJobName(text(root, "jobName"));
                    parsedBuildMetadata.setTimestamp_generation(text(root, "timestampGeneration"));
                    parsedBuildMetadata.setTotalFailure(parsedReport.getTotalFailures());
                    parsedBuildMetadata.setTotalTests(parsedReport.getTotalTests());
                    parsedBuildMetadata.setTotalSkipped(parsedReport.getTotalSkipped());
                    parsedBuildMetadata.setTotalPassed(parsedReport.getTotalPassed());
                }
            }

            return parsedBuildMetadata;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Build metadata", e);
        }
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }
}
