package com.example.model;

public class TestSummary {
    private String timestamp;
    private PerformanceStatus status;
    private String domain;
    private String testType;

    public TestSummary() {
    }

    public TestSummary(String timestamp, PerformanceStatus status, String domain, String testType) {
        this.timestamp = timestamp;
        this.status = status;
        this.domain = domain;
        this.testType = testType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStatus(PerformanceStatus status) {
        this.status = status;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
