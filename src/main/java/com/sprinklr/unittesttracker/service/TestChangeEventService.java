package com.sprinklr.unittesttracker.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.sprinklr.unittesttracker.mapper.TestChangeEventMapper;
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

    public void trackChanges(ParsedTestReport parsedTestReport) {
        try {
            List<TestChangeEventDocument> testChangeEventDocuments = testChangeEventMapper.toTestChangeEventDocuments(parsedTestReport);
            testChangeEventRepository.saveAll(testChangeEventDocuments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to track test changes: " + e.getMessage(), e);
        }
    }
}
