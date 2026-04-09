package com.example.demo.domains.cards;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.core.consumer.BaseKafkaConsumer;
import com.example.demo.properties.ApplicationProperties;
import com.example.model.ResponseMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CardsConsumer extends BaseKafkaConsumer {
    private static final String DOMAIN_NAME = "CARDS";

    private final ApplicationProperties applicationProperties;
    public final ConcurrentHashMap<String, ResponseMetadata> cardResponseTopicResults = new ConcurrentHashMap<>();

    public CardsConsumer(ApplicationProperties applicationProperties, ObjectMapper objectMapper) {
        super(objectMapper);
        this.applicationProperties = applicationProperties;
    }

    @KafkaListener(topics = "${kafka.topics.cardResponse}")
    public void handleCardsResponse(ConsumerRecord<String, String> record) {
        String correlationId = extractCorrelationId(record.value());
        if (correlationId == null) {
            return;
        }

        handleResponse(applicationProperties.getCardsResponseTopic(), correlationId, record.value(), record.timestamp());
    }

    @Override
    protected String getDomainName() {
        return DOMAIN_NAME;
    }

    @Override
    protected ConcurrentHashMap<String, ResponseMetadata> getResponseMapForTopic(String topic) {
        if (applicationProperties.getCardsResponseTopic().equals(topic)) {
            return cardResponseTopicResults;
        }
        return null;
    }

    @Override
    protected void clearAllResponseMaps() {
        cardResponseTopicResults.clear();
    }
}
