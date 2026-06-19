package com.sprinklr.unittesttracker.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.sprinklr.unittesttracker.model.TestDocument;

import java.util.List;
import java.util.Optional;

public interface TestDocumentRepository extends ElasticsearchRepository<TestDocument, String> {
    Optional<TestDocument> findByTestID(String testID);
    Optional<TestDocument> deleteByTestID(String testID);
}
