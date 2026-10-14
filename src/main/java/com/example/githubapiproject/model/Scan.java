package com.example.githubapiproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "scans")
public class Scan {

    @Id
    private String id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int repositoriesDiscovered;

    private int repositoriesScanned;

    private String status;

    public Scan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getRepositoriesDiscovered() {
        return repositoriesDiscovered;
    }

    public void setRepositoriesDiscovered(
            int repositoriesDiscovered) {

        this.repositoriesDiscovered =
                repositoriesDiscovered;
    }

    public int getRepositoriesScanned() {
        return repositoriesScanned;
    }

    public void setRepositoriesScanned(
            int repositoriesScanned) {

        this.repositoriesScanned =
                repositoriesScanned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}