package com.example.githubapiproject.service;

import com.example.githubapiproject.model.LanguageStatisticsResponse;
import com.example.githubapiproject.model.Repository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LanguageService {

    private final RepositoryService repositoryService;

    public LanguageService(
            RepositoryService repositoryService) {

        this.repositoryService = repositoryService;
    }

    public List<LanguageStatisticsResponse>    getLanguageStatistics() {

        // Step 1: Retrieve tracked repositories.
        List<Repository> repositories =
                repositoryService.getAllRepositories();

        int totalRepositories = repositories.size();

        // Step 2: Create a map for language counts.
        Map<String, Integer> languageCounts =
                new HashMap<>();

        // Step 3: Count repositories by language.
        for (Repository repository : repositories) {

            String language =
                    repository.getPrimaryLanguage();

            // Handle missing language information.
            if (language == null || language.isBlank()) {
                language = "Unknown";
            }

            languageCounts.put(language,languageCounts.getOrDefault(language, 0) + 1);
        }

        // Step 4: Create response objects.
        List<LanguageStatisticsResponse> statistics =
                new ArrayList<>();

        for (Map.Entry<String, Integer> entry
                : languageCounts.entrySet()) {

            String language = entry.getKey();
            int count = entry.getValue();

            double percentage =
                    ((double) count / totalRepositories) * 100;

            LanguageStatisticsResponse result =
                    new LanguageStatisticsResponse(
                            language,
                            count,
                            percentage
                    );

            statistics.add(result);
        }

        // Step 5: Sort from most common to least common.
        statistics.sort(
                Comparator.comparingInt(
                        LanguageStatisticsResponse::getRepositoryCount
                ).reversed()
        );

        return statistics;
    }
}
