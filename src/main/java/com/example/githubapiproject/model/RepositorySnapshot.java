
package com.example.githubapiproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reccovery_snapshots")
public class RepositorySnapshot {

    @Id
    private String id;

    private String repositoryId;
    private String scanId;

    private LocalDateTime scanDate;

    private String fullName;
    private String primaryLanguage;
    private boolean newRepository;

    private Long starCount;
    private Long forkCount;

    public RepositorySnapshot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public LocalDateTime getScanDate() {
        return scanDate;
    }

    public void setScanDate(LocalDateTime scanDate) {
        this.scanDate = scanDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public boolean isNewRepository() {
        return newRepository;
    }

    public void setNewRepository(boolean newRepository) {
        this.newRepository = newRepository;
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
