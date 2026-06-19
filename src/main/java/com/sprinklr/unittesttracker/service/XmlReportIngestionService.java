package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestChangeEventMapper;
import com.sprinklr.unittesttracker.mapper.TestMapper;
import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.parser.JUnitParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestChangeEventRepository;
import com.sprinklr.unittesttracker.repository.TestDocumentRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class XmlReportIngestionService {

    private final JUnitParser jUnitParser;
    private final TestChangeEventService testChangeEventService;
    private final TestMapper testMapper;
    private final TestDocumentRepository testDocumentRepository;

    public XmlReportIngestionService(JUnitParser jUnitParser, TestChangeEventService testChangeEventService, TestMapper testMapper, TestChangeEventMapper testChangeEventMapper, TestDocumentRepository testDocumentRepository, TestChangeEventRepository testChangeEventRepository) {
        this.jUnitParser = jUnitParser;
        this.testChangeEventService = testChangeEventService;
        this.testMapper = testMapper;
        this.testDocumentRepository = testDocumentRepository;
    }

    @Async
    public void ingestJunit(MultipartFile reportFile, MultipartFile testInfoFile) {
        try {
            ParsedTestReport parsedReport = jUnitParser.parseFiles(reportFile, testInfoFile);
            List<TestDocument> testDocuments = testMapper.toTestDocuments(parsedReport);
            testChangeEventService.trackChanges(parsedReport);
            testDocumentRepository.saveAll(testDocuments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to ingest JUnit report: " + e.getMessage(), e);
        }
    }
}