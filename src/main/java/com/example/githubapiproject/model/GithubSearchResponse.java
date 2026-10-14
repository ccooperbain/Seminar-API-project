package com.example.githubapiproject.model;

import java.util.List;

public class GithubSearchResponse {

    private int total_count;
    private boolean incomplete_results;
    private List<GithubRepositoryResponse> items;

    public GithubSearchResponse(){
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<GithubRepositoryResponse> getItems() {
        return items;
    }

    public void setItems(List<GithubRepositoryResponse> items) {
        this.items = items;
    }
}
