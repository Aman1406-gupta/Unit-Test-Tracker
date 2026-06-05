package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.JUnitXmlParser;
import com.sprinklr.unittesttracker.parser.MetadataParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class XmlReportIngestionService {
    private final JUnitXmlParser parser;
    private final TestDocumentMapper mapper;
    private final TestExecutionRepository repository;

    public XmlReportIngestionService(JUnitXmlParser parser, TestDocumentMapper mapper, TestExecutionRepository repository) {
        this.parser = parser;
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<TestExecutionDocument> ingestXmlReport_Multipart(MultipartFile reportFile, MultipartFile metadataFile) {
        ParsedTestReport parsedReport = parser.parseFiles(reportFile, metadataFile);
        if(metadataFile != null && !metadataFile.isEmpty()) {
            try {
                MetadataParser.parse_metadata(parsedReport, metadataFile);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to read metadata file: " + e.getMessage());
            }
        }

        List<TestExecutionDocument> documents = mapper.toDocuments(parsedReport);
        repository.saveAll(documents);

        return documents;
    }
}