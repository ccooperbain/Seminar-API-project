package com.example.githubapiproject.service;

import com.example.githubapiproject.model.GithubRepositoryResponse;
import com.example.githubapiproject.model.GithubSearchResponse;
import com.example.githubapiproject.model.GithubCommitActivityResponse;
import com.example.githubapiproject.model.GithubCommitActivityResult;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;


@Service
public class GithubService {

    private final RestClient restClient;
    public GithubService(){
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public GithubRepositoryResponse getRepository(String owner, String repositoryName){
        System.out.println("The owner is " + owner + "The repo name is " + repositoryName);
        return restClient.get()
                .uri("/repos/{owner}/{repo}",owner,repositoryName)
                .retrieve()
                .body(GithubRepositoryResponse.class);
    }

    public GithubSearchResponse searchRepository(String query,int page, int perPage){
        return restClient.get().uri(uriBuilder -> uriBuilder
                .path("/search/repositories")
                .queryParam("q",query)
                .queryParam("sort","stars")
                .queryParam("order","desc")
                .queryParam("page",page)
                .queryParam("per_page",perPage)
                .build())
                .retrieve().body(GithubSearchResponse.class);
    }

    public GithubCommitActivityResult getCommitActivity(String owner,String repo) {

        ResponseEntity<GithubCommitActivityResponse[]> response =
                restClient.get()
                        .uri(
                                "/repos/{owner}/{repo}/stats/commit_activity",
                                owner,
                                repo
                        )
                        .retrieve()
                        .toEntity(GithubCommitActivityResponse[].class);

        HttpStatusCode status = response.getStatusCode();

        // GitHub is still preparing statistics.
        if (status.value() == 202) {

            return new GithubCommitActivityResult(
                    "PROCESSING",
                    "GitHub is calculating commit statistics. Try again later.",
                    List.of()
            );
        }

        // GitHub returned no content.
        if (status.value() == 204) {

            return new GithubCommitActivityResult(
                    "NO_CONTENT",
                    "GitHub returned no commit activity data.",
                    List.of()
            );
        }

        GithubCommitActivityResponse[] body =
                response.getBody();

        // Handle a successful response with an empty body.
        if (body == null) {

            return new GithubCommitActivityResult(
                    "NO_CONTENT",
                    "The response did not contain commit statistics.",
                    List.of()
            );
        }

        return new GithubCommitActivityResult(
                "READY",
                "Commit activity retrieved successfully.",
                Arrays.asList(body)
        );
    }

}
