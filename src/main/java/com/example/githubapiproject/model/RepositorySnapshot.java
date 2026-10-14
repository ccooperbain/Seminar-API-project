package com.example.githubapiproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reccovery_snapshots")
public class RepositorySnapshot {
    @Id
    private String id;

    private String repositoryId;
    private LocalDateTime scanDate;
    private Long starCount;
    private Long forkCount;
    public RepositorySnapshot(){
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getRepositoryId(){
        return repositoryId;
    }
    public void setRepositoryId(String repositoryId){
        this.repositoryId = repositoryId;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(LocalDateTime scanDate) {
        this.scanDate = scanDate;
    }

    public Long getStarCount() {
        return starCount;
    }

    public void setStarCount(Long starCount) {
        this.starCount = starCount;
    }

    public Long getForkCount() {
        return forkCount;
    }

    public void setForkCount(Long forkCount) {
        this.forkCount = forkCount;
    }
}
