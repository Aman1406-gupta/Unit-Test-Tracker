package com.sprinklr.unittesttracker.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.Metadata;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class MetadataParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static void parse_metadata(ParsedTestReport parsedReport, MultipartFile metadataFile) {
        try {
            String content = new String(metadataFile.getBytes(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(content);

            Metadata metadata = new Metadata();
            metadata.setRepositoryUrl(text(root, "repositoryUrl"));
            metadata.setBranchName(text(root, "branchName"));
            metadata.setCommitID(text(root, "commitID"));
            metadata.setBuildID(text(root, "buildID"));
            metadata.setJobName(text(root, "jobName"));
            metadata.setBuildUrl(text(root, "buildUrl"));
            metadata.setTestReportPath(text(root, "testReportPath"));

            JsonNode ts = root.get("timestamp_generation");
            if (ts != null && !ts.isNull() && !ts.asText().isBlank()) {
                metadata.setTimestamp_generation(Instant.parse(ts.asText()));
            }

            parsedReport.setMetadata(metadata);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse metadata file", e);
        }
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }
}
