package com.example.githubapiproject.model;


import java.util.List;

public class GithubCommitActivityResponse {

    private int total;
    private long week;
    private List<Integer> days;

    public GithubCommitActivityResponse() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public long getWeek() {
        return week;
    }

    public void setWeek(long week) {
        this.week = week;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }
}
