package com.example.demo.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.model.PerformanceResult;
import com.example.model.PerformanceStatus;
import com.example.model.ResponseMetadata;

public final class PerformanceResultCalculator {
    private PerformanceResultCalculator() {
    }

    public static PerformanceResult calculate(
        String source,
        Map<String, ResponseMetadata> requests,
        Map<String, ResponseMetadata> responses
    ) {
        List<Double> responseDurations = calculateResponseDurations(requests, responses);
        int successfulResponses = responseDurations.size();
        double averageSeconds = calculateAverageSeconds(responseDurations);
        double percentile90Seconds = calculatePercentile90Seconds(responseDurations);
        double tps = calculateTps(requests, responses);
        double completionRate = calculateCompletionRate(successfulResponses, requests.size());
        PerformanceStatus status = determineStatus(completionRate);

        return new PerformanceResult(
            source,
            averageSeconds,
            percentile90Seconds,
            tps,
            requests.size(),
            completionRate,
            status,
            responseDurations
        );
    }

    static List<Double> calculateResponseDurations(
        Map<String, ResponseMetadata> requests,
        Map<String, ResponseMetadata> responses
    ) {
        List<Double> durations = new ArrayList<>();

        for (String key : requests.keySet()) {
            ResponseMetadata requestMetadata = requests.get(key);
            ResponseMetadata responseMetadata = responses.get(key);

            if (requestMetadata == null || responseMetadata == null) {
                continue;
            }

            durations.add((double) (responseMetadata.getTimestamp() - requestMetadata.getTimestamp()));
        }

        return durations;
    }

    static double calculateAverageSeconds(List<Double> durations) {
        return durations.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) / 1000.0;
    }

    static double calculatePercentile90Seconds(List<Double> durations) {
        if (durations.isEmpty()) {
            return 0.0;
        }

        List<Double> sorted = durations.stream().sorted().toList();
        int index = Math.max((int) Math.ceil(sorted.size() * 0.9) - 1, 0);
        return sorted.get(index) / 1000.0;
    }

    static double calculateTps(Map<String, ResponseMetadata> requests, Map<String, ResponseMetadata> responses) {
        long start = requests.values().stream().mapToLong(ResponseMetadata::getTimestamp).min().orElse(System.currentTimeMillis());
        long end = responses.values().stream().mapToLong(ResponseMetadata::getTimestamp).max().orElse(start);
        long durationSeconds = Math.max((end - start) / 1000L, 1L);
        return requests.size() / (double) durationSeconds;
    }

    static double calculateCompletionRate(int successfulResponses, int totalRequests) {
        if (totalRequests == 0) {
            return 0.0;
        }
        return (successfulResponses / (double) totalRequests) * 100.0;
    }

    static PerformanceStatus determineStatus(double completionRate) {
        if (completionRate >= 90.0) {
            return PerformanceStatus.SUCCESS;
        }
        if (completionRate >= 50.0) {
            return PerformanceStatus.PARTIAL_SUCCESS;
        }
        return PerformanceStatus.RESPONSES_MISSING;
    }
}
