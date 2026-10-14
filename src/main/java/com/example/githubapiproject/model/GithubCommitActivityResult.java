package com.example.githubapiproject.model;


import java.util.List;

public class GithubCommitActivityResult {

    private String status;
    private String message;
    private List<GithubCommitActivityResponse> weeks;

    public GithubCommitActivityResult() {
    }

    public GithubCommitActivityResult(
            String status,
            String message,
            List<GithubCommitActivityResponse> weeks) {

        this.status = status;
        this.message = message;
        this.weeks = weeks;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<GithubCommitActivityResponse> getWeeks() {
        return weeks;
    }
}
