package com.sprinklr.unittesttracker.repository;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestExecutionRepository extends ElasticsearchRepository<TestExecutionDocument, String> {
    Optional<TestExecutionDocument> getReportById(String id);
    List<TestExecutionDocument> getReportsByBuildID(String buildID);
    List<TestExecutionDocument> getReportsByStatus(String status);
}