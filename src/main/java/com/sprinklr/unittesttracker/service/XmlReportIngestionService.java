package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.BuildMetadataMapper;
import com.sprinklr.unittesttracker.mapper.TestChangeEventMapper;
import com.sprinklr.unittesttracker.mapper.TestMapper;
import com.sprinklr.unittesttracker.model.BuildMetadataDocument;
import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import com.sprinklr.unittesttracker.model.TestDocument;
import com.sprinklr.unittesttracker.parser.JUnitParser;
import com.sprinklr.unittesttracker.parser.BuildMetadataParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedBuildMetadata;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.BuildMetadataRepository;
import com.sprinklr.unittesttracker.repository.TestChangeEventRepository;
import com.sprinklr.unittesttracker.repository.TestDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class XmlReportIngestionService {

    private final JUnitParser jUnitParser;
    private final BuildMetadataParser buildMetadataParser;
    private final TestChangeEventService testChangeEventService;
    private final TestMapper testMapper;
    private final BuildMetadataMapper buildMetadataMapper;
    private final TestDocumentRepository testDocumentRepository;
    private final BuildMetadataRepository buildMetadataRepository;

    public XmlReportIngestionService(JUnitParser jUnitParser, BuildMetadataParser buildMetadataParser, TestChangeEventService testChangeEventService, TestMapper testMapper, TestChangeEventMapper testChangeEventMapper, BuildMetadataMapper buildMetadataMapper, TestDocumentRepository testDocumentRepository, TestChangeEventRepository testChangeEventRepository, BuildMetadataRepository buildMetadataRepository) {
        this.jUnitParser = jUnitParser;
        this.buildMetadataParser = buildMetadataParser;
        this.testChangeEventService = testChangeEventService;
        this.testMapper = testMapper;
        this.buildMetadataMapper = buildMetadataMapper;
        this.testDocumentRepository = testDocumentRepository;
        this.buildMetadataRepository = buildMetadataRepository;
    }

    public List<Object> ingestJunit(MultipartFile reportFile, MultipartFile buildmetadataFile, MultipartFile testInfoFile) {
        ParsedTestReport parsedReport = jUnitParser.parseFiles(reportFile, testInfoFile);
        ParsedBuildMetadata parsedBuildMetadata = buildMetadataParser.parseBuildMetadata(buildmetadataFile, parsedReport);

        List<BuildMetadataDocument> buildDocuments = buildMetadataMapper.toBuildDocuments(parsedBuildMetadata);
        List<TestDocument> testDocuments = testMapper.toTestDocuments(parsedReport, parsedBuildMetadata);

        testDocumentRepository.saveAll(testDocuments);
        buildMetadataRepository.saveAll(buildDocuments);

        testChangeEventService.trackChanges_ingestionMode(parsedReport, parsedBuildMetadata, testDocuments);
        System.out.println("Saved testChangeEventDocuments");
        
        return List.of(buildDocuments, testDocuments);
    }
}