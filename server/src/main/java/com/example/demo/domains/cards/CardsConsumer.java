package com.example.demo.domains.cards;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.example.model.*;
import com.example.demo.core.consumer.*;
import com.example.demo.properties.*;

@Component
public class CardsConsumer extends BaseKafkaConsumer {
  private final ApplicationProperties applicationProperties; 
  public final ConcurrentHashMap<String, ResponseMetadata> cardResponseTopicResults = new ConcurrentHashMap<>();

  public CardsConsumer(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @KafkaListener(topics = applicationProperties.getKafkaTopicsCardResponse())
  public void handleCashResponse(ConsumerRecord<String, String> record) {
        String correlationId = extractCorrelationId(record.value());
        if (correlationId != null) {
              handleResponse("ACS_CASH_RESPONSE", correlationId, record.value(), record.timestamp());
          }
    }

  @Override
      protected String getDomainName() {
        return "CARDS";
    }

  @Override
      protected ConcurrentHashMap<String, ResponseMetadata> getResponseMapForTopic(String topic) {
        return switch (topic) {
              case applicationProperties.getKafkaTopicsCardResponse() -> cardResponseTopicResults;
              default -> null;
          };
    }

  @Override
      protected void clearAllResponseMaps() {
        cardResponseTopicResults.clear();
    }
}
