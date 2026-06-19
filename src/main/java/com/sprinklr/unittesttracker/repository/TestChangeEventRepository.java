package com.sprinklr.unittesttracker.repository;

import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestChangeEventRepository extends ElasticsearchRepository<TestChangeEventDocument, String> {
}
