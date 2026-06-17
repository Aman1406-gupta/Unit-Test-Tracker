package com.sprinklr.unittesttracker.mapper;

import com.sprinklr.unittesttracker.model.BuildMetadataDocument;
import com.sprinklr.unittesttracker.parser.parseroutputobjects.ParsedBuildMetadata;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

@Component
public class BuildMetadataMapper {

    public List<BuildMetadataDocument> toBuildDocuments(ParsedBuildMetadata parsedBuildMetadata) {

        List<BuildMetadataDocument> buildDocuments = new ArrayList<>();

        BuildMetadataDocument document = new BuildMetadataDocument();

        String ID = parsedBuildMetadata.getBuildID() + "_" + parsedBuildMetadata.getSuiteName();

        document.setID(ID);
        document.setTotalTests(parsedBuildMetadata.getTotalTests());
        document.setTotalFailure(parsedBuildMetadata.getTotalFailure());
        document.setTotalSkipped(parsedBuildMetadata.getTotalSkipped());
        document.setTotalPassed(parsedBuildMetadata.getTotalPassed());
        document.setCommitSha(parsedBuildMetadata.getCommitSha());
        document.setSuiteName(parsedBuildMetadata.getSuiteName());
        document.setBuildID(parsedBuildMetadata.getBuildID());
        document.setRepositoryUrl(parsedBuildMetadata.getRepositoryUrl());
        document.setBranchName(parsedBuildMetadata.getBranchName());
        document.setJobName(parsedBuildMetadata.getJobName());
        document.setBuildUrl(parsedBuildMetadata.getBuildUrl());
        document.setTimestamp_generation(parsedBuildMetadata.getTimestamp_generation());

        buildDocuments.add(document);

        return buildDocuments;
    }
}