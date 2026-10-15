package com.example.githubapiproject.service;

import com.example.githubapiproject.model.Scan;
import com.example.githubapiproject.repository.ScanRepository;
import com.example.githubapiproject.model.GithubRepositoryResponse;
import com.example.githubapiproject.model.GithubSearchResponse;
import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.DiscoveryResponse;
import com.example.githubapiproject.repository.RepositoryRepository;
import com.example.githubapiproject.model.RepositorySnapshot;
import com.example.githubapiproject.repository.RepositorySnapshotRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class ScanService {

    private final ScanRepository scanRepository;
    private final GithubService githubService;
    private final RepositoryService repositoryService;
    private final RepositoryRepository repositoryRepository;
    private final RepositorySnapshotRepository snapshotRepository;

    public ScanService(
            ScanRepository scanRepository,
            GithubService githubService,
            RepositoryService repositoryService,
            RepositoryRepository repositoryRepository,
            RepositorySnapshotRepository snapshotRepository) {

        this.scanRepository = scanRepository;
        this.githubService = githubService;
        this.repositoryService = repositoryService;
        this.repositoryRepository = repositoryRepository;
        this.snapshotRepository = snapshotRepository;
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

        int repositoriesDiscovered = 0;
        int repositoriesScanned = 0;
        int failedRepositories = 0;

        try {

            // Step 1: Search for recent GitHub repositories.
            LocalDate startDate = LocalDate.now().minusDays(30);

            String searchQuery =
                    "created:>=" + startDate + " stars:>=10";

            GithubSearchResponse searchResponse =
                    githubService.searchRepository(
                            searchQuery,
                            1,
                            20
                    );

            if (searchResponse == null ||
                    searchResponse.getItems() == null) {

                throw new IllegalStateException(
                        "GitHub search did not return repository data"
                );
            }

            // Step 2: Remember which repositories were
            // already in MongoDB BEFORE importing new ones.
            Set<Long> existingGithubIds =
                    repositoryRepository.findAll()
                            .stream()
                            .map(Repository::getGithubId)
                            .collect(Collectors.toSet());

            // Track newly discovered repositories in this scan.
            Set<Long> newGithubIds = new HashSet<>();

            for (GithubRepositoryResponse githubRepo :
                    searchResponse.getItems()) {

                if (githubRepo.getId() != null &&
                        !existingGithubIds.contains(
                                githubRepo.getId())) {

                    newGithubIds.add(githubRepo.getId());
                }
            }

            // Step 3: Import new repositories.
            DiscoveryResponse discoveryResult =
                    repositoryService.discoverRepositories(
                            searchResponse.getItems()
                    );

            repositoriesDiscovered =
                    discoveryResult.getRepositoriesFound();

            System.out.println(
                    "New repositories: " + newGithubIds.size()
            );

            // Step 4: Scan all tracked repositories.
            List<Repository> repositories =
                    repositoryService.getAllRepositories();

            int maxRepositoriesPerScan = 30;

            if (repositories.size() > maxRepositoriesPerScan) {
                repositories = repositories.subList(
                        0,
                        maxRepositoriesPerScan
                );
            }

            for (Repository repository : repositories) {

                try {

                    GithubRepositoryResponse githubRepository =
                            githubService.getRepository(
                                    repository.getOwner(),
                                    repository.getName()
                            );

                    if (githubRepository == null) {
                        throw new IllegalStateException(
                                "GitHub returned no repository data"
                        );
                    }

                    // Update current repository metadata.
                    repositoryService.updateRepositoryFromGithub(
                            repository,
                            githubRepository
                    );

                    // Determine whether this repository was
                    // first discovered during this scan.
                    boolean isNew = newGithubIds.contains(
                            repository.getGithubId()
                    );

                    // Save snapshot with scan history details.
                    repositoryService.createSnapshotFromGithub(
                            repository,
                            githubRepository,
                            scan.getId(),
                            isNew
                    );

                    repositoriesScanned++;

                    System.out.println(
                            "Scanned: " + repository.getFullName() +
                                    " | New: " + isNew
                    );

                } catch (Exception exception) {

                    failedRepositories++;

                    System.err.println(
                            "Failed to scan " +
                                    repository.getFullName() +
                                    ": " +
                                    exception.getMessage()
                    );
                }
            }

            // Step 5: Complete the scan.
            Scan completedScan = completeScan(
                    scan,
                    repositoriesDiscovered,
                    repositoriesScanned
            );

            if (failedRepositories > 0) {

                completedScan.setStatus("PARTIAL");
                completedScan = scanRepository.save(completedScan);
            }

            return completedScan;

        } catch (Exception exception) {

            scan.setEndTime(LocalDateTime.now());
            scan.setRepositoriesDiscovered(repositoriesDiscovered);
            scan.setRepositoriesScanned(repositoriesScanned);
            scan.setStatus("FAILED");

            scanRepository.save(scan);

            throw exception;
        }
    }

    public List<RepositorySnapshot> getScanHistory() {

        return snapshotRepository
                .findTop500ByScanIdIsNotNullOrderByScanDateDesc();
    }

    public List<RepositorySnapshot> getSnapshotsForScan(
            String scanId) {

        return snapshotRepository
                .findByScanIdOrderByScanDateAsc(scanId);
    }


}