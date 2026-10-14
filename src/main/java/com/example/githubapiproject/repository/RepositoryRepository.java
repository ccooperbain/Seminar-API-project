package com.example.githubapiproject.repository;

import com.example.githubapiproject.model.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface RepositoryRepository extends MongoRepository <Repository, String>{
    Optional<Repository> findByGithubId(Long githubId);
    boolean existsByGithubId(Long githubId);
}
