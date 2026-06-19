package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMapper {

    public List<TestDocument> toTestDocuments(ParsedTestReport parsedTestReport) {
        List<TestDocument> testDocuments = new ArrayList<>();
        Instant baseline = Instant.parse("2026-06-17T13:40:09Z");
        Instant oneWeekAgo = baseline.minus(7, ChronoUnit.DAYS);

        for (ParsedTestClass parsedTestClass : parsedTestReport.getTestClasses()) {
            for (ParsedTestCase parsedTestCase : parsedTestClass.getTestCases()) {
                TestDocument document = new TestDocument();
                List<String> owners = new ArrayList<>();
                    try {
                        String command = String.format(
                                "git log --format=%%aI|%%an -L %d,%d:%s",
                                parsedTestCase.getStartLine(),
                                parsedTestCase.getEndLine(),
                                parsedTestCase.getTestCaseFilePath()
                        );
                        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
                        Process process = pb.start();

                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] split = line.split("\\|");
                                if (split.length == 2) {
                                    String timestampStr = split[0].trim();
                                    String authorName = split[1].trim();

                                    try {
                                        Instant commitTime = Instant.parse(timestampStr);
                                        if (commitTime.isAfter(oneWeekAgo) && commitTime.isBefore(baseline)) {
                                            owners.add(authorName);
                                        }
                                    } catch (Exception parseEx) {
                                        if (owners.size() < 6) {
                                            owners.add(authorName);
                                        }
                                    }
                                }
                            }
                        process.waitFor();
                        } catch (Exception e) {
                            if (parsedTestCase.getLastModifiedBy() != null) {
                                owners.add(parsedTestCase.getLastModifiedBy());
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to retrieve owners from git log", e);
                    }

                if (owners.isEmpty() && parsedTestCase.getLastModifiedBy() != null) {
                    owners.add(parsedTestCase.getLastModifiedBy());
                }

                if (owners.size() > 6) {
                    owners = owners.subList(0, 6);
                }

                document.setTestID(parsedTestCase.getTestID());
                document.setBuildID(parsedTestReport.getBuildID());
                document.setRepositoryUrl(parsedTestReport.getRepository_url());
                document.setBranchName(parsedTestReport.getBranchName());
                document.setJobName(parsedTestReport.getJobName());
                document.setClassName(parsedTestCase.getClassName());
                document.setMethodName(parsedTestCase.getMethodName());
                document.setSuiteName(parsedTestReport.getSuiteName());
                document.setStatus(parsedTestCase.getStatus());
                document.setDuration(parsedTestCase.getDuration());
                document.setStackTrace(parsedTestCase.getStackTrace());
                document.setErrorMessage(parsedTestCase.getErrorMessage());
                document.setTimestampExecution(parsedTestCase.getTimestamp_execution());
                document.setTestCaseFilePath(parsedTestCase.getTestCaseFilePath());
                document.setModuleName(parsedTestCase.getModuleName());
                document.setStartLine(parsedTestCase.getStartLine());
                document.setEndLine(parsedTestCase.getEndLine());
                document.setOwners(owners);
                document.setOwnershipSource(parsedTestCase.getOwnershipSource());
                document.setConfidenceScore(parsedTestCase.getConfidenceScore());
                document.setCreatedAt(parsedTestCase.getCreatedAt());
                document.setLastModifiedAt(parsedTestCase.getLastModifiedAt());
                document.setLastModifiedBy(parsedTestCase.getLastModifiedBy());
                document.setCurrentLifecycleStatus(parsedTestCase.getCurrentLifecycleStatus());
                document.setCurrentCommitSha(parsedTestCase.getCurrentCommitSha());

                testDocuments.add(document);
            }
        }
        return testDocuments;
    }
}