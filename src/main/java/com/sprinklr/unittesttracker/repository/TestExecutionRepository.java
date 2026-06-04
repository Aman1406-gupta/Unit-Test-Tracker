package com.sprinklr.unittesttracker.repository;
import com.sprinklr.unittesttracker.model.TestExecutionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestExecutionRepository extends ElasticsearchRepository<TestExecutionDocument, String> {
    List<TestExecutionDocument> findByTestName(String testName);
    List<TestExecutionDocument> findByTestClass(String testClass);
    List<TestExecutionDocument> findBySuiteName(String suiteName);
    List<TestExecutionDocument> findByBuildID(String buildID);
    List<TestExecutionDocument> findByStatus(String status);
}