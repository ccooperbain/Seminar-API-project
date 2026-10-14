package com.example.githubapiproject.scheduler;



import com.example.githubapiproject.service.ScanService;
import com.example.githubapiproject.service.CommitService;

import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.CommitImportResponse;

import com.example.githubapiproject.service.RepositoryService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class GithubScanScheduler {

    private final ScanService scanService;
    private final CommitService commitService;
    private final RepositoryService repositoryService;

    private final AtomicBoolean scanRunning =
            new AtomicBoolean(false);

    @Value("${github.scan.enabled:false}")
    private boolean scanEnabled;

    @Value("${github.scan.max-commit-imports:10}")
    private int maxCommitImports;

    public GithubScanScheduler(
            ScanService scanService,
            CommitService commitService,
            RepositoryService repositoryService) {

        this.scanService = scanService;
        this.commitService = commitService;
        this.repositoryService = repositoryService;
    }

    @Scheduled(
            cron = "${github.scan.cron}",
            zone = "America/New_York"
    )
    public void runScheduledScan() {

        if (!scanEnabled) {
            return;
        }

        // Prevent overlapping scheduled executions.
        if (!scanRunning.compareAndSet(false, true)) {
            System.out.println(
                    "A scheduled scan is already running."
            );
            return;
        }

        try {

            System.out.println(
                    "Starting scheduled GitHub scan..."
            );

            // Step 1: Run repository discovery and snapshots.
            scanService.runScan();

            // Step 2: Retrieve tracked repositories.
            List<Repository> repositories =
                    repositoryService.getAllRepositories();

            // Step 3: Import commit statistics.
            int importsAttempted = 0;

            for (Repository repository : repositories) {

                if (importsAttempted >= maxCommitImports) {
                    break;
                }

                try {

                    CommitImportResponse result =
                            commitService.importCommitActivity(
                                    repository.getId()
                            );

                    System.out.println(
                            "Commit import for "
                                    + repository.getFullName()
                                    + ": "
                                    + result.getStatus()
                    );

                } catch (Exception exception) {

                    System.err.println(
                            "Commit import failed for "
                                    + repository.getFullName()
                                    + ": "
                                    + exception.getMessage()
                    );
                }

                importsAttempted++;
            }

            System.out.println(
                    "Scheduled GitHub scan finished."
            );

        } catch (Exception exception) {

            System.err.println(
                    "Scheduled scan failed: "
                            + exception.getMessage()
            );

        } finally {

            scanRunning.set(false);
        }
    }
}
