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

    public Repository importGithubRepository(
            GithubRepositoryResponse githubRepository) {

        return importGithubRepository(githubRepository, true);
    }

    public Repository importGithubRepository(
            GithubRepositoryResponse githubRepository,
            boolean createSnapshot) {

        Optional<Repository> existingRepository =
                repositoryRepository.findByGithubId(
                        githubRepository.getId()
                );

        Repository repository;

        if (existingRepository.isPresent()) {

            repository = existingRepository.get();

        } else {

            repository = convertGithubRepository(githubRepository);
            repository = repositoryRepository.save(repository);
        }

        if (createSnapshot) {
            createSnapshotFromGithub(repository, githubRepository);
        }

        return repository;
    }

    public DiscoveryResponse discoverRepositories(
            List<GithubRepositoryResponse> githubRepositories) {

        int repositoriesFound = githubRepositories.size();
        int newRepositories = 0;
        int existingRepositories = 0;

        for (GithubRepositoryResponse githubRepository : githubRepositories) {

            Optional<Repository> existingRepository =
                    repositoryRepository.findByGithubId(
                            githubRepository.getId()
                    );

            if (existingRepository.isEmpty()) {

                // The scan loop will create the snapshot later.
                importGithubRepository(githubRepository, false);
                newRepositories++;

            } else {

                existingRepositories++;
            }
        }

        return new DiscoveryResponse(
                repositoriesFound,
                newRepositories,
                existingRepositories
        );
    }


    public RepositorySnapshot createSnapshotFromGithub(
            Repository repository,
            GithubRepositoryResponse githubRepository) {

        // Preserve compatibility with existing manual imports.
        return createSnapshotFromGithub(
                repository,
                githubRepository,
                null,
                false
        );
    }

    public RepositorySnapshot createSnapshotFromGithub(
            Repository repository,
            GithubRepositoryResponse githubRepository,
            String scanId,
            boolean newRepository) {

        RepositorySnapshot snapshot =
                new RepositorySnapshot();

        snapshot.setRepositoryId(repository.getId());
        snapshot.setScanId(scanId);
        snapshot.setScanDate(LocalDateTime.now());

        snapshot.setFullName(githubRepository.getFull_name());

        snapshot.setPrimaryLanguage(
                githubRepository.getLanguage()
        );

        snapshot.setNewRepository(newRepository);

        snapshot.setStarCount(
                githubRepository.getStargazers_count()
        );

        snapshot.setForkCount(
                githubRepository.getForks_count()
        );

        return snapshotRepository.save(snapshot);
    }



    public Repository updateRepositoryFromGithub(
            Repository repository,
            GithubRepositoryResponse githubRepository) {

        repository.setName(githubRepository.getName());
        repository.setFullName(githubRepository.getFull_name());
        repository.setUrl(githubRepository.getHtml_url());
        repository.setDescription(githubRepository.getDescription());
        repository.setPrimaryLanguage(githubRepository.getLanguage());

        String fullName = githubRepository.getFull_name();

        if (fullName != null && fullName.contains("/")) {
            repository.setOwner(fullName.split("/", 2)[0]);
        }

        return repositoryRepository.save(repository);
    }

}
