package com.example.githubapiproject.model;


public class CommitImportResponse {

    private String status;
    private String message;
    private int recordsCreated;
    private int recordsUpdated;

    public CommitImportResponse() {
    }

    public CommitImportResponse(
            String status,
            String message,
            int recordsCreated,
            int recordsUpdated) {

        this.status = status;
        this.message = message;
        this.recordsCreated = recordsCreated;
        this.recordsUpdated = recordsUpdated;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getRecordsCreated() {
        return recordsCreated;
    }

    public int getRecordsUpdated() {
        return recordsUpdated;
    }
}
