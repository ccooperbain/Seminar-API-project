package com.example.githubapiproject.service;


import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.RepositoryGrowthResponse;
import com.example.githubapiproject.model.TrendingRepositoryResponse;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TrendingService {

    private final RepositoryService repositoryService;

    private final RepositorySnapshotService snapshotService;

    public TrendingService(
            RepositoryService repositoryService,
            RepositorySnapshotService snapshotService) {

        this.repositoryService = repositoryService;
        this.snapshotService = snapshotService;
    }

    public List<TrendingRepositoryResponse>    getTrendingRepositories() {

        // Step 1: Retrieve all tracked repositories.
        List<Repository> repositories =
                repositoryService.getAllRepositories();

        // Step 2: Create a list for ranking results.
        List<TrendingRepositoryResponse> trending =
                new ArrayList<>();

        // Step 3: Calculate growth for each repository.
        for (Repository repository : repositories) {

            RepositoryGrowthResponse growth =
                    snapshotService.calculateStarGrowth(
                            repository.getId()
                    );

            // Skip repositories without enough history.
            if (growth == null) {
                continue;
            }

            TrendingRepositoryResponse result =
                    new TrendingRepositoryResponse(
                            0,
                            repository.getId(),
                            repository.getFullName(),
                            repository.getUrl(),
                            repository.getPrimaryLanguage(),
                            growth.getPreviousStars(),
                            growth.getCurrentStars(),
                            growth.getStarGrowth()
                    );

            trending.add(result);
        }

        // Step 4: Sort by growth, highest first.
        trending.sort(
                Comparator.comparing(
                        TrendingRepositoryResponse::getStarGrowth
                ).reversed()
        );

        // Step 5: Assign ranking positions.
        for (int i = 0; i < trending.size(); i++) {
            trending.get(i).setRank(i + 1);
        }

        return trending;
    }
}