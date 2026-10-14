package com.example.githubapiproject.model;


import java.time.LocalDateTime;

public class RepositoryGrowthResponse {

    private String repositoryId;
    private Long previousStars;
    private Long currentStars;
    private Long starGrowth;
    private LocalDateTime previousDate;
    private LocalDateTime currentDate;

    public RepositoryGrowthResponse() {
    }

    public RepositoryGrowthResponse(
            String repositoryId,
            Long previousStars,
            Long currentStars,
            Long starGrowth,
            LocalDateTime previousDate,
            LocalDateTime currentDate) {

        this.repositoryId = repositoryId;
        this.previousStars = previousStars;
        this.currentStars = currentStars;
        this.starGrowth = starGrowth;
        this.previousDate = previousDate;
        this.currentDate = currentDate;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public Long getPreviousStars() {
        return previousStars;
    }

    public Long getCurrentStars() {
        return currentStars;
    }

    public Long getStarGrowth() {
        return starGrowth;
    }

    public LocalDateTime getPreviousDate() {
        return previousDate;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }
}