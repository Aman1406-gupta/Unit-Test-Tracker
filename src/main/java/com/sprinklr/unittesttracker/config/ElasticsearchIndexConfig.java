package com.sprinklr.unittesttracker.config;

import com.sprinklr.unittesttracker.model.TestChangeEventDocument;
import com.sprinklr.unittesttracker.model.TestDocument;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

@Configuration
public class ElasticsearchIndexConfig {

    private final ElasticsearchOperations elasticsearchOperations;

    public ElasticsearchIndexConfig(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostConstruct
    public void createIndexes() {
        IndexOperations changeEventOps = elasticsearchOperations.indexOps(TestChangeEventDocument.class);
        if (!changeEventOps.exists()) {
            changeEventOps.create();
            changeEventOps.putMapping(changeEventOps.createMapping());
            System.out.println("Successfully provisioned index mapping structure for: test_change_event");
        }

        IndexOperations testDocOps = elasticsearchOperations.indexOps(TestDocument.class);
        if (!testDocOps.exists()) {
            testDocOps.create();
            testDocOps.putMapping(testDocOps.createMapping());
            System.out.println("Successfully provisioned index mapping structure for: test");
        }
    }
}