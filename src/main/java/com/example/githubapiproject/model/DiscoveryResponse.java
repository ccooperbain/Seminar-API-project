package com.example.githubapiproject.model;

public class DiscoveryResponse {

    private int repositoriesFound;

    private int newRepositories;

    private int existingRepositories;

    public DiscoveryResponse() {
    }

    public DiscoveryResponse(
            int repositoriesFound,
            int newRepositories,
            int existingRepositories) {

        this.repositoriesFound = repositoriesFound;
        this.newRepositories = newRepositories;
        this.existingRepositories = existingRepositories;
    }

    public int getRepositoriesFound() {
        return repositoriesFound;
    }

    public void setRepositoriesFound(
            int repositoriesFound) {

        this.repositoriesFound = repositoriesFound;
    }

    public int getNewRepositories() {
        return newRepositories;
    }

    public void setNewRepositories(
            int newRepositories) {

        this.newRepositories = newRepositories;
    }

    public int getExistingRepositories() {
        return existingRepositories;
    }

    public void setExistingRepositories(
            int existingRepositories) {

        this.existingRepositories =
                existingRepositories;
    }
}