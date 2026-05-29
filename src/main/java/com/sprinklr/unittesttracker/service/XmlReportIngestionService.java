package com.sprinklr.unittesttracker.service;

import com.sprinklr.unittesttracker.mapper.TestDocumentMapper;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import com.sprinklr.unittesttracker.parser.JUnitXmlParser;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedTestReport;
import com.sprinklr.unittesttracker.repository.TestExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
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

    public String ingestXmlReport(MultipartFile file, String buildID, String commitID, String branchName) throws Exception {
        String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        ParsedTestReport parsedReport = parser.parse(xmlContent, buildID, commitID, branchName);
        List<TestExecutionDocument> documents = mapper.toDocuments_xml(parsedReport);
        repository.saveAll(documents);

        return "XML report ingested successfully. Total test cases: " + documents.size();
    }
}
