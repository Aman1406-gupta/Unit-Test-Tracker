package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.JUnitXmlParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
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

    public String ingestXmlReport(String xmlContent) {
        ParsedTestReport parsedReport = parser.parse(xmlContent);
        List<TestExecutionDocument> documents = mapper.toDocuments(parsedReport);
        repository.saveAll(documents);

        return "XML report ingested successfully. Total test cases: " + documents.size();
    }
}