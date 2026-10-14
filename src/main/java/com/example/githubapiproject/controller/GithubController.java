package com.example.githubapiproject.controller;

import com.example.githubapiproject.model.GithubRepositoryResponse;
import com.example.githubapiproject.service.GithubService;
import com.example.githubapiproject.service.RepositoryService;
import com.example.githubapiproject.model.Repository;
import com.example.githubapiproject.model.GithubSearchResponse;
import com.example.githubapiproject.model.DiscoveryResponse;
import com.example.githubapiproject.model.GithubCommitActivityResult;



import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;



@RestController
@RequestMapping("/api/github")
public class GithubController {

    private final GithubService githubService;
    private final RepositoryService repositoryService;

    public GithubController(GithubService githubService, RepositoryService repositoryService) {
        this.githubService = githubService;
        this.repositoryService = repositoryService;
    }

    @GetMapping("/repos/{owner}/{repositoryName}")
    public GithubRepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        return githubService.getRepository(owner, repositoryName);
    }

    @PostMapping("/repos/{owner}/{repositoryName}/import")
    public Repository importRepository(@PathVariable String owner, @PathVariable String repositoryName) {
        GithubRepositoryResponse githubRepository = githubService.getRepository(owner, repositoryName);
        return repositoryService.importGithubRepository(githubRepository);
    }

    @GetMapping("/repos/{owner}/{repo}/commits")
    public ResponseEntity<GithubCommitActivityResult>
    getCommitActivity(
            @PathVariable String owner,
            @PathVariable String repo) {

        GithubCommitActivityResult result =
                githubService.getCommitActivity(owner, repo);

        if ("PROCESSING".equals(result.getStatus())) {

            return ResponseEntity
                    .status(202)
                    .body(result);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/discover")
    public DiscoveryResponse discoverRepositories(@RequestParam(defaultValue = "stars:>1000")
                                                 String query,
                                                 @RequestParam(defaultValue = "1")
                                                 int page,
                                                 @RequestParam(defaultValue = "10")
                                                 int perPage) {
        GithubSearchResponse searchResponse = githubService.searchRepository(query, page, perPage);
        return repositoryService.discoverRepositories(searchResponse.getItems());
    }

    @GetMapping("/search")
    public GithubSearchResponse searchRepositories(
            @RequestParam (defaultValue = "stars:>1000") String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage){
        return githubService.searchRepository(query,page,perPage);
    }
    


}
