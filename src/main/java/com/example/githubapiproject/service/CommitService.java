package com.example.githubapiproject.service;


import com.example.githubapiproject.model.CommitImportResponse;
import com.example.githubapiproject.model.GithubCommitActivityResponse;
import com.example.githubapiproject.model.GithubCommitActivityResult;
import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.WeeklyCommitStats;

import com.example.githubapiproject.repository.RepositoryRepository;
import com.example.githubapiproject.repository.WeeklyCommitStatsRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class CommitService {

    private final WeeklyCommitStatsRepository commitRepository;
    private final RepositoryRepository repositoryRepository;
    private final GithubService githubService;

    public CommitService(
            WeeklyCommitStatsRepository commitRepository,
            RepositoryRepository repositoryRepository,
            GithubService githubService) {

        this.commitRepository = commitRepository;
        this.repositoryRepository = repositoryRepository;
        this.githubService = githubService;
    }

    public List<WeeklyCommitStats> getCommitsForRepository(
            String repositoryId) {

        return commitRepository
                .findByRepositoryIdOrderByWeekAsc(repositoryId);
    }

    public CommitImportResponse importCommitActivity(
            String repositoryId) {

        // Step 1: Find the repository in MongoDB.
        Repository repository = repositoryRepository.findById(repositoryId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Repository not found"
                                )
                        );

        // Step 2: Retrieve statistics from GitHub.
        GithubCommitActivityResult result = githubService.getCommitActivity(
                        repository.getOwner(),
                        repository.getName()
                );

        // Step 3: Handle statistics that are not ready.
        if ("PROCESSING".equals(result.getStatus())) {

            return new CommitImportResponse(
                    "PROCESSING",
                    "GitHub is still calculating commit statistics.",
                    0,
                    0
            );
        }

        if (!"READY".equals(result.getStatus())) {

            return new CommitImportResponse(
                    result.getStatus(),
                    result.getMessage(),
                    0,
                    0
            );
        }

        // Step 4: Count new and updated records.
        int recordsCreated = 0;
        int recordsUpdated = 0;

        // Step 5: Process each week.
        for (GithubCommitActivityResponse githubWeek
                : result.getWeeks()) {

            WeeklyCommitStats existing =
                    commitRepository
                            .findByRepositoryIdAndWeek(
                                    repositoryId,
                                    githubWeek.getWeek()
                            )
                            .orElse(null);

            WeeklyCommitStats stats;

            if (existing == null) {

                stats = new WeeklyCommitStats();

                stats.setRepositoryId(repositoryId);
                stats.setWeek(githubWeek.getWeek());

                recordsCreated++;

            } else {

                stats = existing;

                recordsUpdated++;
            }

            // Step 6: Copy the latest GitHub statistics.
            stats.setTotal(githubWeek.getTotal());
            stats.setDays(githubWeek.getDays());

            // Step 7: Save the record.
            commitRepository.save(stats);
        }

        return new CommitImportResponse(
                "COMPLETED",
                "Weekly commit statistics imported successfully.",
                recordsCreated,
                recordsUpdated
        );
    }
}
