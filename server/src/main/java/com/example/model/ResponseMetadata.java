package com.example.model;

public class ResponseMetadata {
    private long timestamp;
    private PerformanceStatus status;
    private String message;

    public ResponseMetadata() {
    }

    public ResponseMetadata(long timestamp) {
        this(timestamp, PerformanceStatus.SUCCESS, "");
    }

    public ResponseMetadata(long timestamp, PerformanceStatus status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStatus(PerformanceStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
