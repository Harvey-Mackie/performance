package com.example.model;

public class ProgressResult {
    private String status;
    private String progress;
    private String testId;

    public ProgressResult() {
    }

    public ProgressResult(String status, String progress, String testId) {
        this.status = status;
        this.progress = progress;
        this.testId = testId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }
}
