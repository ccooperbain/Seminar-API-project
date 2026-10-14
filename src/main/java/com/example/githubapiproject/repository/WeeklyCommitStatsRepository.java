package com.example.githubapiproject.repository;


import com.example.githubapiproject.model.WeeklyCommitStats;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyCommitStatsRepository
        extends MongoRepository<WeeklyCommitStats, String> {

    List<WeeklyCommitStats> findByRepositoryIdOrderByWeekAsc(String repositoryId);

    Optional<WeeklyCommitStats> findByRepositoryIdAndWeek(String repositoryId,long week);
}
