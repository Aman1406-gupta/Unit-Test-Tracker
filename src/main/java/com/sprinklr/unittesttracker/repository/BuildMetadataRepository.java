package com.sprinklr.unittesttracker.repository;
import com.sprinklr.unittesttracker.model.BuildMetadataDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildMetadataRepository extends ElasticsearchRepository<BuildMetadataDocument, String> {
}