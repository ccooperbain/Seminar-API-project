package com.example.githubapiproject.service;

import com.example.githubapiproject.model.Scan;
import com.example.githubapiproject.repository.ScanRepository;
import com.example.githubapiproject.model.GithubRepositoryResponse;
import com.example.githubapiproject.model.GithubSearchResponse;
import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.DiscoveryResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScanService {

    private final ScanRepository scanRepository;
    private final GithubService githubService;
    private final RepositoryService repositoryService;

    public ScanService(ScanRepository scanRepository,  GithubService githubService, RepositoryService repositoryService) {
        this.scanRepository = scanRepository;
        this.githubService = githubService;
        this.repositoryService = repositoryService;
    }

    public List<Scan> getAllScans() {
        return scanRepository.findAll();
    }

    public Scan getScanById(String id) {
        return scanRepository.findById(id)
                .orElse(null);
    }

    public Scan saveScan(Scan scan) {
        return scanRepository.save(scan);
    }

    public Scan startScan() {

        Scan scan = new Scan();

        scan.setStartTime(
                LocalDateTime.now()
        );

        scan.setStatus("RUNNING");

        return scanRepository.save(scan);
    }

    public Scan completeScan(
            Scan scan,
            int repositoriesDiscovered,
            int repositoriesScanned) {

        scan.setEndTime(
                LocalDateTime.now()
        );

        scan.setRepositoriesDiscovered(
                repositoriesDiscovered
        );

        scan.setRepositoriesScanned(
                repositoriesScanned
        );

        scan.setStatus("COMPLETED");

        return scanRepository.save(scan);
    }

    public Scan runScan() {

        Scan scan = startScan();

        try {

            GithubSearchResponse searchResponse =
                    githubService.searchRepository(
                            "stars:>1000",
                            1,
                            10
                    );

            DiscoveryResponse discoveryResult =
                    repositoryService.discoverRepositories(
                            searchResponse.getItems()
                    );

            List<Repository> repositories =
                    repositoryService.getAllRepositories();

            int repositoriesScanned = 0;

            for (Repository repository : repositories) {

                GithubRepositoryResponse githubRepository =
                        githubService.getRepository(
                                repository.getOwner(),
                                repository.getName()
                        );

                repositoryService.createSnapshotFromGithub(
                        repository,
                        githubRepository
                );

                repositoriesScanned++;
            }

            return completeScan(
                    scan,
                    discoveryResult.getRepositoriesFound(),
                    repositoriesScanned
            );

        } catch (Exception exception) {

            scan.setEndTime(
                    LocalDateTime.now()
            );

            scan.setStatus("FAILED");

            scanRepository.save(scan);

            throw exception;
        }
    }
}