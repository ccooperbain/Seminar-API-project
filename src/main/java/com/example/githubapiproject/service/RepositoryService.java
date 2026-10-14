package com.example.githubapiproject.service;

import com.example.githubapiproject.model.DiscoveryResponse;
import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.repository.RepositoryRepository;
import com.example.githubapiproject.model.GithubRepositoryResponse;
import com.example.githubapiproject.model.RepositorySnapshot;
import com.example.githubapiproject.repository.RepositorySnapshotRepository;


import com.example.githubapiproject.repository.RepositorySnapshotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RepositoryService {
    private final RepositorySnapshotRepository snapshotRepository;
    private final RepositoryRepository repositoryRepository;
    public RepositoryService(RepositoryRepository repositoryRepository,RepositorySnapshotRepository snapshotRepository) {
        this.repositoryRepository = repositoryRepository;
        this.snapshotRepository = snapshotRepository;

    }

    public List<Repository> getAllRepositories(){
        return repositoryRepository.findAll();
    }
    public Optional<Repository> getRepositoriesById(String id){
        return repositoryRepository.findById(id);
    }
    public Repository saveRepository(Repository repository){
        return repositoryRepository.save(repository);
    }
    public void deleteRepository(String id){
        repositoryRepository.deleteById(id);
    }

    public Repository convertGithubRepository(GithubRepositoryResponse githubRepositoryResponse){
        Repository repository = new Repository();

        repository.setGithubId(githubRepositoryResponse.getId());
        repository.setName(githubRepositoryResponse.getName());
        repository.setFullName(githubRepositoryResponse.getFull_name());
        repository.setUrl(githubRepositoryResponse.getHtml_url());
        repository.setDescription(githubRepositoryResponse.getDescription());
        repository.setPrimaryLanguage(githubRepositoryResponse.getLanguage());

        String fullname = githubRepositoryResponse.getFull_name();

        if(fullname != null && fullname.contains("/")){
            repository.setOwner(fullname.substring(fullname.lastIndexOf("/")+0));
            String[] parts = fullname.split("/");
            repository.setOwner(parts[0]);
            System.out.println("The owner is ["+repository.getOwner()+"]");
            System.out.println("the full name is ["+githubRepositoryResponse.getFull_name()+"]");
        }
        return repository;
    }

    public Repository importGithubRepository(GithubRepositoryResponse githubRepository){
        Repository repository;

        Optional<Repository> existingRepository = repositoryRepository.findByGithubId(githubRepository.getId());

        if (existingRepository.isPresent()) {
            repository = existingRepository.get();
        }else{
            repository = convertGithubRepository(githubRepository);
            repository = repositoryRepository.save(repository);
        }

        RepositorySnapshot snapshot = new RepositorySnapshot();
        snapshot.setRepositoryId(repository.getId());
        snapshot.setScanDate(LocalDateTime.now());
        snapshot.setStarCount(githubRepository.getStargazers_count());
        snapshot.setForkCount(githubRepository.getForks_count());
        snapshotRepository.save(snapshot);

        return repository;
    }

    public DiscoveryResponse discoverRepositories(List<GithubRepositoryResponse> githubRepositories){
        int repositoriesFound = githubRepositories.size();
        int newRepositories = 0;
        int existingRepositories = 0;



        for(GithubRepositoryResponse githubRepository : githubRepositories){
            Optional<Repository> existingRepository = repositoryRepository.findByGithubId(githubRepository.getId());

            if (existingRepository.isEmpty()) {
                importGithubRepository(githubRepository);
                newRepositories++;
            }else{
                existingRepositories++;
            }
        }
        return new DiscoveryResponse(repositoriesFound,newRepositories,existingRepositories);
    }

    public RepositorySnapshot createSnapshotFromGithub(
            Repository repository,
            GithubRepositoryResponse githubRepository) {

        RepositorySnapshot snapshot =
                new RepositorySnapshot();

        snapshot.setRepositoryId(
                repository.getId()
        );

        snapshot.setScanDate(
                LocalDateTime.now()
        );

        snapshot.setStarCount(
                githubRepository.getStargazers_count()
        );

        snapshot.setForkCount(
                githubRepository.getForks_count()
        );

        return snapshotRepository.save(snapshot);
    }

}
