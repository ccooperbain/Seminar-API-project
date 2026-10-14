package com.example.githubapiproject.model;


public class TrendingRepositoryResponse {

    private int rank;
    private String repositoryId;
    private String fullName;
    private String url;
    private String primaryLanguage;
    private Long previousStars;
    private Long currentStars;
    private Long starGrowth;

    public TrendingRepositoryResponse() {
    }

    public TrendingRepositoryResponse(
            int rank,
            String repositoryId,
            String fullName,
            String url,
            String primaryLanguage,
            Long previousStars,
            Long currentStars,
            Long starGrowth) {

        this.rank = rank;
        this.repositoryId = repositoryId;
        this.fullName = fullName;
        this.url = url;
        this.primaryLanguage = primaryLanguage;
        this.previousStars = previousStars;
        this.currentStars = currentStars;
        this.starGrowth = starGrowth;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrl() {
        return url;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
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
}