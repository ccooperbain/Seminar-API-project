package com.example.githubapiproject.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.util.List;

@Document(collection = "weekly_commit_stats")
@CompoundIndex(
        name = "repository_week_unique",
        def = "{'repositoryId': 1, 'week': 1}",
        unique = true
)
public class WeeklyCommitStats {

    @Id
    private String id;

    private String repositoryId;

    private long week;

    private int total;

    private List<Integer> days;

    public WeeklyCommitStats() {
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

    public long getWeek() {
        return week;
    }

    public void setWeek(long week) {
        this.week = week;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }
}
