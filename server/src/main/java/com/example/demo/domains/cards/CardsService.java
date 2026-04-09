package com.example.demo.domains.cards;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.core.messageBuilder.MessageContainer;
import com.example.demo.core.results.ResultsManager;
import com.example.demo.core.service.PerformanceResultCalculator;
import com.example.demo.core.transport.KafkaTransportStrategy;
import com.example.demo.core.transport.TransportDestination;
import com.example.demo.core.transport.TransportPayload;
import com.example.demo.properties.ApplicationProperties;
import com.example.model.PerformanceResult;
import com.example.model.PerformanceSummary;
import com.example.model.ProgressResult;
import com.example.model.ResponseMetadata;

@Component
public class CardsService {
    private static final Logger log = LoggerFactory.getLogger(CardsService.class);
    private static final String DOMAIN_NAME = "cards";
    private static final String TEST_TYPE = "load";
    private static final String SOURCE_NAME = "cards";
    private static final String COMPLETE = "100%";

    private final ApplicationProperties applicationProperties;
    private final CardsMessageBuilder cardsMessageBuilder;
    private final KafkaTransportStrategy kafkaTransportStrategy;
    private final CardsConsumer cardsConsumer;
    private final ResultsManager resultsManager;

    private final Map<String, CopyOnWriteArrayList<Object>> progressEvents = new ConcurrentHashMap<>();
    private volatile String testInProgress;

    public CardsService(
        ApplicationProperties applicationProperties,
        CardsMessageBuilder cardsMessageBuilder,
        KafkaTransportStrategy kafkaTransportStrategy,
        CardsConsumer cardsConsumer,
        ResultsManager resultsManager
    ) {
        this.applicationProperties = applicationProperties;
        this.cardsMessageBuilder = cardsMessageBuilder;
        this.kafkaTransportStrategy = kafkaTransportStrategy;
        this.cardsConsumer = cardsConsumer;
        this.resultsManager = resultsManager;
    }

    public synchronized ProgressResult startLoadTestAsync() {
        if (testInProgress != null) {
            log.warn("A cards test is already in progress: {}", testInProgress);
            return new ProgressResult("Failed to start load test as one is already running", null, testInProgress);
        }

        String testId = LocalDateTime.now().toString();
        progressEvents.put(testId, new CopyOnWriteArrayList<>());
        testInProgress = testId;

        Thread worker = new Thread(() -> loadTest(testId), "cards-load-test-" + testId);
        worker.setDaemon(true);
        worker.start();

        return new ProgressResult("Successfully started load test", null, testId);
    }

    public SseEmitter streamProgress(String testId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        Thread worker = new Thread(() -> {
            int sentCount = 0;
            boolean completed = false;

            while (!completed) {
                CopyOnWriteArrayList<Object> events = progressEvents.getOrDefault(testId, new CopyOnWriteArrayList<>());

                while (sentCount < events.size()) {
                    Object event = events.get(sentCount++);
                    try {
                        emitter.send(event);
                        if (event instanceof PerformanceSummary || isTerminalProgress(event)) {
                            emitter.complete();
                            completed = true;
                            break;
                        }
                    } catch (IOException exception) {
                        emitter.completeWithError(exception);
                        completed = true;
                        break;
                    }
                }

                if (!completed) {
                    sleepQuietly(500L);
                }
            }
        }, "cards-progress-" + testId);
        worker.setDaemon(true);
        worker.start();

        return emitter;
    }

    private void loadTest(String testId) {
        cardsConsumer.reset();
        cardsConsumer.enableAcceptance();

        try {
            addProgress(testId, new ProgressResult("Building messages", "0%", testId));
            MessageContainer cardMessages = cardsMessageBuilder.buildCardsMessages(applicationProperties.getCardsMessageCount());
            Map<String, ResponseMetadata> cardRequests = new ConcurrentHashMap<>();

            addProgress(testId, new ProgressResult("Sending messages", "5%", testId));
            CompletableFuture<Void> cardTask = sendMessagesAsync(
                cardMessages,
                applicationProperties.getCardsRequestTopic(),
                cardRequests
            );
            CompletableFuture.allOf(cardTask).join();

            addProgress(testId, new ProgressResult("Receiving responses", "10%", testId));
            Awaitility.await()
                .atMost(applicationProperties.getCardsTimeout(), TimeUnit.MINUTES)
                .pollInterval(applicationProperties.getCardsPollInterval(), TimeUnit.SECONDS)
                .until(() -> {
                    int totalExpected = cardMessages.getCorrelationIds().size();
                    int totalReceived = cardsConsumer.cardResponseTopicResults.size();
                    int percent = totalExpected == 0 ? 0 : (int) ((totalReceived * 100.0) / totalExpected);

                    addProgress(testId, new ProgressResult("Receiving responses", percent + "%", testId));
                    return totalExpected == totalReceived;
                });

            PerformanceResult result = PerformanceResultCalculator.calculate(
                SOURCE_NAME,
                cardRequests,
                cardsConsumer.cardResponseTopicResults
            );

            PerformanceSummary summary = new PerformanceSummary();
            summary.setTimestamp(LocalDateTime.now().toString());
            summary.setCombinedResult(result);
            summary.setPerSourceResults(List.of(result));

            resultsManager.saveResult(DOMAIN_NAME, TEST_TYPE, summary);
            addProgress(testId, new ProgressResult("Completed", COMPLETE, testId));
            addProgress(testId, summary);
        } catch (Exception exception) {
            log.error("Cards load test failed", exception);
            addProgress(testId, new ProgressResult("Failed: " + exception.getMessage(), COMPLETE, testId));
        } finally {
            cardsConsumer.disableAcceptance();
            cardsConsumer.reset();
            testInProgress = null;
        }
    }

    private CompletableFuture<Void> sendMessagesAsync(
        MessageContainer messageContainer,
        String topic,
        Map<String, ResponseMetadata> requestTracker
    ) {
        return CompletableFuture.runAsync(() -> {
            TransportDestination destination = new TransportDestination(topic);
            List<String> correlationIds = messageContainer.getCorrelationIds();
            List<String> messages = messageContainer.getMessages();

            for (int index = 0; index < correlationIds.size(); index++) {
                String correlationId = correlationIds.get(index);
                String message = messages.get(index);

                kafkaTransportStrategy.sendMessage(destination, TransportPayload.single(correlationId, message));
                requestTracker.put(correlationId, new ResponseMetadata(System.currentTimeMillis()));
            }
        });
    }

    private void addProgress(String testId, Object event) {
        progressEvents.computeIfAbsent(testId, ignored -> new CopyOnWriteArrayList<>()).add(event);
    }

    private boolean isTerminalProgress(Object event) {
        if (!(event instanceof ProgressResult progressResult)) {
            return false;
        }
        return progressResult.getStatus() != null
            && (progressResult.getStatus().startsWith("Failed") || "Completed".equals(progressResult.getStatus()));
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
