package com.sprinklr.unittesttracker.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.sprinklr.unittesttracker.model.TestDocument;
import java.util.Optional;

public interface TestDocumentRepository extends ElasticsearchRepository<TestDocument, String> {
    Optional<TestDocument> findByTestIDAndBuildID(String testID, String buildID);
}
