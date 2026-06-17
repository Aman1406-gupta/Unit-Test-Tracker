package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedBuildMetadata;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.repository.TestChangeEventRepository;
import com.sprinklr.unittesttracker.repository.TestDocumentRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.time.Instant;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestChangeEventMapper {

    @Autowired
    private TestDocumentRepository testDocumentRepository;

    @Autowired
    private TestChangeEventRepository testChangeEventRepository;

    private static final Pattern COMMIT_SHA_PATTERN = Pattern.compile("^[0-9a-fA-F]{40}$");

    public List<TestChangeEventDocument> toTestChangeEventDocuments(ParsedTestReport parsedTestReport, ParsedBuildMetadata parsedBuildMetadata, List<TestDocument> testDocuments) {
        List<TestChangeEventDocument> testChangeEventDocuments = new ArrayList<>();
        Map<String, TestDocument> testDocumentMap = testDocuments.stream().collect(Collectors.toMap(TestDocument::getTestID, d -> d, (a, b) -> a));

        for (ParsedTestClass parsedTestClass : parsedTestReport.getTestClasses()) {
            for (ParsedTestCase parsedTestCase : parsedTestClass.getTestCases()) {

                TestDocument testDocument = testDocumentMap.get(parsedTestCase.getTestID());
                if(testDocument == null) {
                    continue;
                }
                GitHistoryResult gitResult = executeGitLog(testDocument);

                if (gitResult.changeType == TestChangeEventDocument.ChangeType.UNCHANGED) {
                    System.out.println("No change");
                }

                TestChangeEventDocument document =testChangeEventRepository.findByTestIDAndBuildID(parsedTestCase.getTestID(), parsedBuildMetadata.getBuildID()).orElseGet(TestChangeEventDocument::new);
                Instant detectedAt = Instant.now();

                document.setTestID(parsedTestCase.getTestID());
                document.setBuildID(parsedBuildMetadata.getBuildID());
                document.setRepositoryURL(parsedBuildMetadata.getRepositoryUrl());
                document.setBranchName(parsedBuildMetadata.getBranchName());
                document.setChangeType(gitResult.changeType);
                document.setPreviousCommitSha(gitResult.previousCommitSha);
                document.setCurrentCommitSha(parsedTestCase.getCurrentCommitSha());
                document.setDetectedAt(detectedAt);

                if(document.getEventID() == null) {
                    String eventID = java.util.UUID.nameUUIDFromBytes((detectedAt.toString() + parsedBuildMetadata.getBuildID() + parsedTestCase.getTestID()).getBytes()).toString();
                    document.setEventID(eventID);
                }

                testChangeEventDocuments.add(document);
            }
        }

        return testChangeEventDocuments;
    }

    public TestChangeEventDocument toTestChangeEvent(String testID, String buildID) {
        TestDocument testDocument = testDocumentRepository.findByTestIDAndBuildID(testID, buildID).orElseThrow(() -> new RuntimeException("Test document not found"));

        GitHistoryResult gitResult = executeGitLog(testDocument);

        if (gitResult.changeType == TestChangeEventDocument.ChangeType.UNCHANGED) {
            System.out.println("No change");
        }

        TestChangeEventDocument document = testChangeEventRepository.findByTestIDAndBuildID(testID, buildID).orElseGet(TestChangeEventDocument::new);
        Instant detectedAt = Instant.now();

        document.setTestID(testID);
        document.setBuildID(buildID);
        document.setChangeType(gitResult.changeType);
        document.setPreviousCommitSha(gitResult.previousCommitSha);
        document.setCurrentCommitSha(gitResult.currentCommitSha);
        document.setDetectedAt(detectedAt);

        if(document.getEventID() == null) {
            String eventID = java.util.UUID.nameUUIDFromBytes((detectedAt.toString() + buildID + testID).getBytes()).toString();
            document.setEventID(eventID);
        }

        return document;
    }

    private GitHistoryResult executeGitLog(TestDocument testDocument) {
        GitHistoryResult result = new GitHistoryResult();

        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("log");
        command.add("-p");
        command.add("--format=%H");
        command.add("-L");
        command.add(String.format("%d,%d:%s", testDocument.getStartLine(), testDocument.getEndLine(), testDocument.getTestCaseFilePath()));

        List<String> commits = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(false);
            Process process = processBuilder.start();

            int addedCount = 0;
            int deletedCount = 0;
            boolean hasDiff = false;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (COMMIT_SHA_PATTERN.matcher(line).matches()) {
                        commits.add(line);
                        if (hasDiff && commits.size() > 2) {
                            break;
                        }
                    }

                    if (line.startsWith("+") && !line.startsWith("+++")) {
                        addedCount++;
                        hasDiff = true;
                    } else if (line.startsWith("-") && !line.startsWith("---")) {
                        deletedCount++;
                        hasDiff = true;
                    }
                }
            }
            process.waitFor();

            if (!commits.isEmpty()) {
                result.currentCommitSha = commits.get(0);
                if (commits.size() > 1) {
                    result.previousCommitSha = commits.get(1);
                }
            }

            if (addedCount > 0 && deletedCount == 0) {
                result.changeType = TestChangeEventDocument.ChangeType.ADDED;
            } else if (addedCount > 0 && deletedCount > 0) {
                result.changeType = TestChangeEventDocument.ChangeType.MODIFIED;
            } else if (deletedCount > 0 && addedCount == 0) {
                result.changeType = TestChangeEventDocument.ChangeType.DELETED;
            }

        } catch (Exception e) {
            result.changeType = TestChangeEventDocument.ChangeType.UNCHANGED;
        }

        return result;
    }

    private static class GitHistoryResult {
        String currentCommitSha = null;
        String previousCommitSha = null;
        TestChangeEventDocument.ChangeType changeType = TestChangeEventDocument.ChangeType.UNCHANGED;
    }
}