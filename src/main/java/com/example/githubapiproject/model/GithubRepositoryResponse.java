package com.example.githubapiproject.model;



public class GithubRepositoryResponse {

    private Long id;
    private String name;

    private String full_name;
    private String html_url;
    private String description;
    private Long stargazers_count;
    private Long forks_count;
    private String language;

    public GithubRepositoryResponse(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(Long stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public Long getForks_count() {
        return forks_count;
    }

    public void setForks_count(Long forks_count) {
        this.forks_count = forks_count;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
