package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestCase;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestClass;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestChangeEventRepository;
import com.sprinklr.unittesttracker.repository.TestDocumentRepository;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.time.Instant;

@Component
public class TestChangeEventMapper {

    @Autowired
    private TestDocumentRepository testDocumentRepository;

    @Autowired
    private TestChangeEventRepository testChangeEventRepository;

    public List<TestChangeEventDocument> toTestChangeEventDocuments(ParsedTestReport parsedTestReport) {
        List<TestChangeEventDocument> testChangeEventDocuments = new ArrayList<>();
        Set<String> processedTestIDsInCurrentBuild = new HashSet<>();

        for (ParsedTestClass parsedTestClass : parsedTestReport.getTestClasses()) {
            for (ParsedTestCase parsedTestCase : parsedTestClass.getTestCases()) {
                String testID = parsedTestCase.getTestID();
                processedTestIDsInCurrentBuild.add(testID);

                TestDocument historyDoc = testDocumentRepository.findByTestID(testID).orElse(null);
                TestChangeEventDocument.ChangeType computedChangeType = TestChangeEventDocument.ChangeType.UNCHANGED;
                String previousStatus = null;
                String previousCommitSha = null;

                if (historyDoc == null) {
                    computedChangeType = TestChangeEventDocument.ChangeType.ADDED;
                } else {
                    previousStatus = historyDoc.getStatus();
                    previousCommitSha = historyDoc.getCurrentCommitSha();

                    int prevLinesSpan = historyDoc.getEndLine() - historyDoc.getStartLine();
                    int currLinesSpan = parsedTestCase.getEndLine() - parsedTestCase.getStartLine();

                    if (prevLinesSpan != currLinesSpan) {
                        computedChangeType = TestChangeEventDocument.ChangeType.MODIFIED;
                    } else {
                        boolean hasContentChanged = checkBlockContentDiff(
                                historyDoc.getTestCaseFilePath(),
                                historyDoc.getStartLine(), historyDoc.getEndLine(),
                                parsedTestCase.getStartLine(), parsedTestCase.getEndLine()
                        );
                        if (hasContentChanged) {
                            computedChangeType = TestChangeEventDocument.ChangeType.MODIFIED;
                        }
                    }
                }

                if (computedChangeType == TestChangeEventDocument.ChangeType.UNCHANGED) {
                    continue;
                }

                TestChangeEventDocument document = new TestChangeEventDocument();
                Instant detectedAt = Instant.now();

                document.setTestID(testID);
                document.setBuildID(parsedTestReport.getBuildID());
                document.setChangeType(computedChangeType);
                document.setPreviousCommitSha(previousCommitSha);
                document.setCurrentCommitSha(parsedTestCase.getCurrentCommitSha());
                document.setPreviousStatus(previousStatus);
                document.setCurrentStatus(parsedTestCase.getStatus());
                document.setDetectedAt(detectedAt);

                String generatedId = java.util.UUID.nameUUIDFromBytes((detectedAt.toString() + parsedTestReport.getBuildID() + testID).getBytes()).toString();
                document.setEventID(generatedId);

                System.out.println("previousStatus: " + previousStatus);
                System.out.println("currentStatus: " + parsedTestCase.getStatus());
                System.out.println("previousCommitSha: " + previousCommitSha);
                System.out.println("currentCommitSha: " + parsedTestCase.getCurrentCommitSha());

                testChangeEventDocuments.add(document);
                System.out.println("Added TestChangeEventDocument for testID: " + testID + " with changeType: " + computedChangeType);
            }
        }

        List<TestDocument> allStoredTests = new ArrayList<>();
        testDocumentRepository.findAll().forEach(allStoredTests::add);

        for (TestDocument storedTest : allStoredTests) {
            if (!processedTestIDsInCurrentBuild.contains(storedTest.getTestID())) {
                TestChangeEventDocument deletedDocument = new TestChangeEventDocument();
                Instant detectedAt = Instant.now();

                deletedDocument.setTestID(storedTest.getTestID());
                deletedDocument.setBuildID(parsedTestReport.getBuildID());
                deletedDocument.setChangeType(TestChangeEventDocument.ChangeType.DELETED);
                deletedDocument.setPreviousCommitSha(storedTest.getCurrentCommitSha());
                deletedDocument.setCurrentCommitSha(null);
                deletedDocument.setPreviousStatus(storedTest.getStatus());
                deletedDocument.setCurrentStatus(null);
                deletedDocument.setDetectedAt(detectedAt);

                String generatedId = java.util.UUID.nameUUIDFromBytes((detectedAt.toString() + parsedTestReport.getBuildID() + storedTest.getTestID()).getBytes()).toString();
                deletedDocument.setEventID(generatedId);

                testChangeEventDocuments.add(deletedDocument);
                System.out.println("Added TestChangeEventDocument for deleted testID: " + storedTest.getTestID() + " with changeType: DELETED");
            }
        }

        return testChangeEventDocuments;
    }

    private boolean checkBlockContentDiff(String filePath, int pStart, int pEnd, int cStart, int cEnd) {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("diff");
        command.add("HEAD");
        command.add("--");
        command.add(filePath);

        try {
            ProcessBuilder pb = new ProcessBuilder(command);

            File gitDir = findGitRoot(new File(System.getProperty("user.dir")));
            if (gitDir != null) {
                pb.directory(gitDir);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            boolean changesInLinesRange = false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                int currentLeftLine = 0;
                int currentRightLine = 0;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("@@")) {
                        String[] parts = line.split(" ");
                        String leftPart = parts[1].substring(1);
                        String rightPart = parts[2].substring(1);
                        currentLeftLine = Integer.parseInt(leftPart.split(",")[0]);
                        currentRightLine = Integer.parseInt(rightPart.split(",")[0]);
                        continue;
                    }

                    if (line.startsWith("-") && !line.startsWith("---")) {
                        if (currentLeftLine >= pStart && currentLeftLine <= pEnd) {
                            changesInLinesRange = true;
                        }
                        currentLeftLine++;
                    } else if (line.startsWith("+") && !line.startsWith("+++")) {
                        if (currentRightLine >= cStart && currentRightLine <= cEnd) {
                            changesInLinesRange = true;
                        }
                        currentRightLine++;
                    } else {
                        currentLeftLine++;
                        currentRightLine++;
                    }
                }
            }
            process.waitFor();
            System.out.println("Sucessfully evaluated text modifications structural block for file: "+ filePath);
            return changesInLinesRange;
        } catch (Exception e) {
            throw new RuntimeException("Failed evaluating text modifications structural block: " + e.getMessage());
        }
    }

    private File findGitRoot(File currentDir) {
        while (currentDir != null) {
            File gitDir = new File(currentDir, ".git");
            if (gitDir.exists() && gitDir.isDirectory()) {
                return currentDir;
            }
            currentDir = currentDir.getParentFile();
        }
        return null;
    }
}