package com.sprinklr.unittesttracker.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.sprinklr.unittesttracker.model.TestDocument;
import java.util.List;
import java.util.Optional;

public interface TestDocumentRepository extends ElasticsearchRepository<TestDocument, String> {
    Optional<TestDocument> findByTestID(String testID);
    long deleteByTestID(String testID);

    @Query("{\"match_all\": {}}")
    @org.springframework.data.elasticsearch.annotations.SourceFilters(includes = "testID")
    List<TestDocument> findALLProjectedBy();
}

