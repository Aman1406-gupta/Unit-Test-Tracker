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
import java.util.*;
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
            }
        }

        List<String> allTestIDsInPreviousBuild = testDocumentRepository.findALLProjectedBy().stream().map(TestDocument::getTestID).toList();
        if (allTestIDsInPreviousBuild == null || allTestIDsInPreviousBuild.isEmpty()) {
            System.out.println("No test documents found in previous build. All tests will be treated as ADDED.");
        }

        for (String testID : allTestIDsInPreviousBuild) {
            if (!processedTestIDsInCurrentBuild.contains(testID)) {
                TestDocument historyDoc = testDocumentRepository.findByTestID(testID).orElse(null);
                if(historyDoc == null) {
                    System.out.println("No history document found for testID: " + testID + ". Skipping deletion tracking.");
                    continue;
                }
                if (historyDoc != null) {
                    TestChangeEventDocument document = new TestChangeEventDocument();
                    Instant detectedAt = Instant.now();

                    document.setTestID(testID);
                    document.setBuildID(parsedTestReport.getBuildID()); // Should I use previous build ID instead? Assuming we want to track the deletion in the context of the current build.
                    document.setChangeType(TestChangeEventDocument.ChangeType.DELETED);
                    document.setPreviousCommitSha(historyDoc.getCurrentCommitSha());
                    document.setCurrentCommitSha(null);
                    document.setPreviousStatus(historyDoc.getStatus());
                    document.setCurrentStatus(null);
                    document.setDetectedAt(detectedAt);

                    String generatedId = java.util.UUID.nameUUIDFromBytes((detectedAt.toString() + parsedTestReport.getBuildID() + testID).getBytes()).toString();
                    document.setEventID(generatedId);

                     long deletedCount = testDocumentRepository.deleteByTestID(testID);
                     System.out.println("Attempting to delete TestDocument for testID: " + testID + ". Deleted count: " + deletedCount);
                     if (deletedCount == 0) {
                         throw new RuntimeException("Failed to delete TestDocument for testID: " + testID);
                     }
                    testChangeEventDocuments.add(document);
                    System.out.println("Added TestChangeEventDocument for deleted testID: " + testID + " with changeType: DELETED");
                    System.out.println("Deleted TestDocument for testID: " + testID + " from repository");
                }
            }
        }

        for (ParsedTestClass parsedTestClass : parsedTestReport.getTestClasses()) {
            for (ParsedTestCase parsedTestCase : parsedTestClass.getTestCases()) {
                String testID = parsedTestCase.getTestID();

                TestDocument historyDoc = testDocumentRepository.findByTestID(testID).orElse(null);
                TestChangeEventDocument.ChangeType computedChangeType = TestChangeEventDocument.ChangeType.UNCHANGED;
                String previousStatus = null;
                String currentStatus;
                String previousCommitSha = null;
                String currentCommitSha;

                if (historyDoc == null) {
                    computedChangeType = TestChangeEventDocument.ChangeType.ADDED;
                } else {
                    previousStatus = historyDoc.getStatus();
                    currentStatus = parsedTestCase.getStatus();
                    previousCommitSha = historyDoc.getCurrentCommitSha();
                    currentCommitSha = parsedTestCase.getCurrentCommitSha();

                    if (previousStatus != null && currentStatus != null) {
                        if (!previousCommitSha.equals(currentCommitSha) || !previousStatus.equals(currentStatus)) {
                            computedChangeType = TestChangeEventDocument.ChangeType.MODIFIED;
                        } else {
                            computedChangeType = TestChangeEventDocument.ChangeType.UNCHANGED;
                            continue;
                        }
                    }
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

                testChangeEventDocuments.add(document);
                System.out.println("Added TestChangeEventDocument for testID: " + testID + " with changeType: " + computedChangeType);
            }
        }
        System.out.println("All changes tracked Successfully");

        return testChangeEventDocuments;
    }
}