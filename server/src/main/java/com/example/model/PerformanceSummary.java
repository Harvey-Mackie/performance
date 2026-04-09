package com.example.model;

import java.util.List;

public class PerformanceSummary {
    private String timestamp;
    private PerformanceResult combinedResult;
    private String environment;
    private List<PerformanceResult> perSourceResults;

    public PerformanceSummary() {
    }

    public PerformanceSummary(
        String timestamp,
        PerformanceResult combinedResult,
        String environment,
        List<PerformanceResult> perSourceResults
    ) {
        this.timestamp = timestamp;
        this.combinedResult = combinedResult;
        this.environment = environment;
        this.perSourceResults = perSourceResults;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public PerformanceResult getCombinedResult() {
        return combinedResult;
    }

    public void setCombinedResult(PerformanceResult combinedResult) {
        this.combinedResult = combinedResult;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public List<PerformanceResult> getPerSourceResults() {
        return perSourceResults;
    }

    public void setPerSourceResults(List<PerformanceResult> perSourceResults) {
        this.perSourceResults = perSourceResults;
    }
}
