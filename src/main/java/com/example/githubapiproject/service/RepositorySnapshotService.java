package com.example.githubapiproject.service;

import com.example.githubapiproject.model.RepositorySnapshot;
import com.example.githubapiproject.repository.RepositorySnapshotRepository;
import com.example.githubapiproject.model.RepositoryGrowthResponse;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;

@Service
public class RepositorySnapshotService {

    private final RepositorySnapshotRepository snapshotRepository;
    public RepositorySnapshotService(RepositorySnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    public List<RepositorySnapshot> getSnapshotsForRepository(String repositoryId) {
        return snapshotRepository.findByRepositoryIdOrderByScanDateAsc(repositoryId);
    }

    public RepositorySnapshot saveSnapshot(RepositorySnapshot snapshot) {
        return snapshotRepository.save(snapshot);
    }

    public RepositorySnapshot getLatestSnapshot(String repositoryId) {
        return snapshotRepository.findTopByRepositoryIdOrderByScanDateDesc(repositoryId);
    }

    public RepositoryGrowthResponse calculateStarGrowth(
            String repositoryId) {

        List<RepositorySnapshot> snapshots =
                snapshotRepository
                        .findByRepositoryIdOrderByScanDateAsc(
                                repositoryId
                        );

        // At least two snapshots are needed.
        if (snapshots.size() < 2) {
            return null;
        }

        // Sort snapshots chronologically.
        snapshots.sort(
                Comparator.comparing(
                        RepositorySnapshot::getScanDate
                )
        );

        // Retrieve the last two snapshots.
        int lastIndex = snapshots.size() - 1;

        RepositorySnapshot currentSnapshot =
                snapshots.get(lastIndex);

        RepositorySnapshot previousSnapshot =
                snapshots.get(lastIndex - 1);

        // Calculate star growth.
        Long starGrowth =
                currentSnapshot.getStarCount()
                        - previousSnapshot.getStarCount();

        // Return the calculated result.
        return new RepositoryGrowthResponse(
                repositoryId,
                previousSnapshot.getStarCount(),
                currentSnapshot.getStarCount(),
                starGrowth,
                previousSnapshot.getScanDate(),
                currentSnapshot.getScanDate()
        );
    }
}
