package com.example.githubapiproject.controller;

import com.example.githubapiproject.model.RepositorySnapshot;
import com.example.githubapiproject.service.RepositorySnapshotService;
import com.example.githubapiproject.model.RepositoryGrowthResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/repositories")
public class RepositorySnapshotController {

    private final RepositorySnapshotService snapshotService;
    public RepositorySnapshotController(RepositorySnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GetMapping("/{repositoryId}/snapshots")
    public List<RepositorySnapshot> getSnapshots(@PathVariable String repositoryId) {
        return snapshotService.getSnapshotsForRepository(repositoryId);
    }

    @GetMapping("/{repositoryId}/growth")
    public ResponseEntity<RepositoryGrowthResponse> getStarGrowth(@PathVariable String repositoryId) {
        RepositoryGrowthResponse growth = snapshotService.calculateStarGrowth(repositoryId);
        if(growth == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(growth);
    }

    @PostMapping("/{repositoryId}/snapshots")
    public RepositorySnapshot createSnapshot(@PathVariable String repositoryId, @RequestBody RepositorySnapshot snapshot) {
        snapshot.setRepositoryId(repositoryId);
        return snapshotService.saveSnapshot(snapshot);
    }

}
