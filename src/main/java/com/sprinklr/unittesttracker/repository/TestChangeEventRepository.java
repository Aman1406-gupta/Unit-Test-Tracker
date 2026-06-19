package com.sprinklr.unittesttracker.repository;

import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.Optional;

public interface TestChangeEventRepository extends ElasticsearchRepository<TestChangeEventDocument, String> {
    Optional<TestChangeEventDocument> findByTestID(String testID);
}
