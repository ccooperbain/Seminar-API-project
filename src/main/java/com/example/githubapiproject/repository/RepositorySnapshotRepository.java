package com.example.githubapiproject.repository;

import com.example.githubapiproject.model.RepositorySnapshot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepositorySnapshotRepository extends MongoRepository <RepositorySnapshot, String> {
    //List<RepositorySnapshot> findByRepositoryIdOrderedByScanDateAsc(String repositoryId);
    List<RepositorySnapshot> findByRepositoryIdOrderByScanDateAsc(String repositoryId);

    RepositorySnapshot findTopByRepositoryIdOrderByScanDateDesc(String repositoryId);

}
