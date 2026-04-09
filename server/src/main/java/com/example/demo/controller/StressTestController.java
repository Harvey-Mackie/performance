package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.core.results.ResultsManager;
import com.example.demo.domains.cards.CardsService;
import com.example.model.PerformanceSummary;
import com.example.model.ProgressResult;
import com.example.model.TestSummary;

@RestController
@RequestMapping("api/v1/cards")
public class StressTestController {
    private final CardsService cardsService;
    private final ResultsManager resultsManager;

    public StressTestController(CardsService cardsService, ResultsManager resultsManager) {
        this.cardsService = cardsService;
        this.resultsManager = resultsManager;
    }

    @PostMapping("/load")
    public ResponseEntity<ProgressResult> loadTest() {
        return ResponseEntity.ok(cardsService.startLoadTestAsync());
    }

    @GetMapping(path = "/load/stream/{testId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamProgress(@PathVariable String testId) {
        return cardsService.streamProgress(testId);
    }

    @GetMapping("/load/list")
    public ResponseEntity<List<TestSummary>> listResults() throws IOException {
        return ResponseEntity.ok(resultsManager.listStoredSummaries("cards", "load"));
    }

    @GetMapping("/load/{timestamp}")
    public ResponseEntity<PerformanceSummary> getResult(@PathVariable String timestamp) throws IOException {
        return resultsManager.loadResult("cards", "load", timestamp)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
