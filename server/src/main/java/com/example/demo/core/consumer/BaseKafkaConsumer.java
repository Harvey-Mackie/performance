// core/BaseKafkaConsumer.java
package com.example.demo.core.consumer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.model.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseKafkaConsumer {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final ObjectMapper objectMapper = new ObjectMapper();
    
    protected volatile boolean acceptingResults = false;
    
    public void enableAcceptance() {
        acceptingResults = true;
        log.info("Enabled acceptance of Kafka responses for {}", getDomainName());
    }
    
    public void disableAcceptance() {
        acceptingResults = false;
        clearAllResponseMaps();
        log.info("Disabled acceptance of Kafka responses and cleared maps for {}", getDomainName());
    }
    
    protected void handleResponse(String topic, String correlationId, String responseData, long timestamp) {
        if (!acceptingResults) return;
        
        try {
            ConcurrentHashMap<String, ResponseMetadata> responseMap = getResponseMapForTopic(topic);
            if (responseMap != null) {
                responseMap.put(correlationId, new ResponseMetadata(timestamp, responseData));
                log.debug("Received {} response for correlationId: {}", topic, correlationId);
            } else {
                log.warn("No response map found for topic: {}", topic);
            }
        } catch (Exception e) {
            log.error("Error processing response for topic: {} and correlationId: {}", topic, correlationId, e);
        }
    }
    
    protected String extractCorrelationId(String messageContent) {
        try {
            JsonNode jsonNode = objectMapper.readTree(messageContent);
            return jsonNode.get("correlationId").asText();
        } catch (Exception e) {
            log.error("Failed to extract correlationId from message: {}", messageContent, e);
            return null;
        }
    }
    
    // Abstract methods that domains must implement
    protected abstract String getDomainName();
    protected abstract ConcurrentHashMap<String, ResponseMetadata> getResponseMapForTopic(String topic);
    protected abstract void clearAllResponseMaps();
}
