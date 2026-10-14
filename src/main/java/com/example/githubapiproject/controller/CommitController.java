package com.example.githubapiproject.controller;


import com.example.githubapiproject.model.CommitImportResponse;
import com.example.githubapiproject.model.WeeklyCommitStats;
import com.example.githubapiproject.service.CommitService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commits")
public class CommitController {

    private final CommitService commitService;

    public CommitController(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("/{repositoryId}")
    public List<WeeklyCommitStats> getCommits(
            @PathVariable String repositoryId) {

        return commitService
                .getCommitsForRepository(repositoryId);
    }

    @PostMapping("/{repositoryId}/import")
    public ResponseEntity<CommitImportResponse> importCommits(
            @PathVariable String repositoryId) {

        CommitImportResponse result =
                commitService.importCommitActivity(repositoryId);

        if ("PROCESSING".equals(result.getStatus())) {

            return ResponseEntity
                    .status(202)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }
}