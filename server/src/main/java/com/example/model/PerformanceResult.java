package com.example.model;

import java.util.List;

public class PerformanceResult {
    private String source;
    private double averageResponseTimeInSeconds;
    private double percentile90ResponseTimeInSeconds;
    private double tps;
    private int requestCount;
    private double completionPercentage;
    private PerformanceStatus status;
    private List<Double> durations;

    public PerformanceResult() {
    }

    public PerformanceResult(
        String source,
        double averageResponseTimeInSeconds,
        double percentile90ResponseTimeInSeconds,
        double tps,
        int requestCount,
        double completionPercentage,
        PerformanceStatus status,
        List<Double> durations
    ) {
        this.source = source;
        this.averageResponseTimeInSeconds = averageResponseTimeInSeconds;
        this.percentile90ResponseTimeInSeconds = percentile90ResponseTimeInSeconds;
        this.tps = tps;
        this.requestCount = requestCount;
        this.completionPercentage = completionPercentage;
        this.status = status;
        this.durations = durations;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getAverageResponseTimeInSeconds() {
        return averageResponseTimeInSeconds;
    }

    public void setAverageResponseTimeInSeconds(double averageResponseTimeInSeconds) {
        this.averageResponseTimeInSeconds = averageResponseTimeInSeconds;
    }

    public double getPercentile90ResponseTimeInSeconds() {
        return percentile90ResponseTimeInSeconds;
    }

    public void setPercentile90ResponseTimeInSeconds(double percentile90ResponseTimeInSeconds) {
        this.percentile90ResponseTimeInSeconds = percentile90ResponseTimeInSeconds;
    }

    public double getTps() {
        return tps;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public PerformanceStatus getStatus() {
        return status;
    }

    public void setStatus(PerformanceStatus status) {
        this.status = status;
    }

    public List<Double> getDurations() {
        return durations;
    }

    public void setDurations(List<Double> durations) {
        this.durations = durations;
    }
}
