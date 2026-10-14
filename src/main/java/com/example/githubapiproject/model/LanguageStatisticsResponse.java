package com.example.githubapiproject.model;


public class LanguageStatisticsResponse {

    private String language;
    private int repositoryCount;
    private double percentage;

    public LanguageStatisticsResponse() {
    }

    public LanguageStatisticsResponse(
            String language,
            int repositoryCount,
            double percentage) {

        this.language = language;
        this.repositoryCount = repositoryCount;
        this.percentage = percentage;
    }

    public String getLanguage() {
        return language;
    }

    public int getRepositoryCount() {
        return repositoryCount;
    }

    public double getPercentage() {
        return percentage;
    }
}
