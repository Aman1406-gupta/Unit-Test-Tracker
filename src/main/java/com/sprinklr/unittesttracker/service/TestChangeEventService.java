package com.sprinklr.unittesttracker.service;

import java.util.List;

import com.sprinklr.unittesttracker.model.TestDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.sprinklr.unittesttracker.mapper.TestChangeEventMapper;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedBuildMetadata;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import com.sprinklr.unittesttracker.repository.TestChangeEventRepository;

@Service
public class TestChangeEventService {

    private final TestChangeEventMapper testChangeEventMapper;
    private final TestChangeEventRepository testChangeEventRepository;

    public TestChangeEventService(TestChangeEventMapper testChangeEventMapper, TestChangeEventRepository testChangeEventRepository) {
        this.testChangeEventMapper = testChangeEventMapper;
        this.testChangeEventRepository = testChangeEventRepository;
    }

    public TestChangeEventDocument trackChange(String testID, String buildID) {
        TestChangeEventDocument testChangeEventDocument = testChangeEventMapper.toTestChangeEvent(testID, buildID);
        testChangeEventRepository.save(testChangeEventDocument);
        return testChangeEventDocument;
    }

    @Async
    public void trackChanges_ingestionMode(ParsedTestReport parsedTestReport, ParsedBuildMetadata parsedBuildMetadata, List<TestDocument> testDocuments) {
        List<TestChangeEventDocument> testChangeEventDocuments = testChangeEventMapper.toTestChangeEventDocuments(parsedTestReport, parsedBuildMetadata, testDocuments);
        testChangeEventRepository.saveAll(testChangeEventDocuments);
    }
}
