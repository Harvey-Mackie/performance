package com.example.demo.core.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.ResponseMetadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseKafkaConsumer {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final ObjectMapper objectMapper;

    protected volatile boolean acceptingResults = false;

    protected BaseKafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void enableAcceptance() {
        acceptingResults = true;
        log.info("Enabled acceptance of Kafka responses for {}", getDomainName());
    }

    public void disableAcceptance() {
        acceptingResults = false;
        log.info("Disabled acceptance of Kafka responses for {}", getDomainName());
    }

    protected void handleResponse(String topic, String correlationId, String responseData, long timestamp) {
        if (!acceptingResults) {
            return;
        }

        try {
            ConcurrentHashMap<String, ResponseMetadata> responseMap = getResponseMapForTopic(topic);
            if (responseMap == null) {
                log.warn("No response map found for topic: {}", topic);
                return;
            }

            responseMap.put(correlationId, new ResponseMetadata(timestamp));
            log.debug("Received {} response for correlationId: {}", topic, correlationId);
        } catch (Exception exception) {
            log.error("Error processing response for topic: {} and correlationId: {}", topic, correlationId, exception);
        }
    }

    protected String extractCorrelationId(String messageContent) {
        try {
            JsonNode jsonNode = objectMapper.readTree(messageContent);
            JsonNode correlationId = jsonNode.get("correlationId");
            return correlationId == null ? null : correlationId.asText();
        } catch (Exception exception) {
            log.error("Failed to extract correlationId from message", exception);
            return null;
        }
    }

    public void reset() {
        clearAllResponseMaps();
    }

    protected abstract String getDomainName();

    protected abstract ConcurrentHashMap<String, ResponseMetadata> getResponseMapForTopic(String topic);

    protected abstract void clearAllResponseMaps();
}
