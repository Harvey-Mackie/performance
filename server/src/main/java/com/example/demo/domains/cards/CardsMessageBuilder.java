package com.example.demo.domains.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.core.messageBuilder.MessageContainer;

public class CardsMessageBuilder {
  
  public MessageContainer buildCardsMessages() {
    var correlationIds = generateCorrelationIds(10); // 10 messages for demo
    List<String> messages = new ArrayList<>();
    
    for (String correlationId : correlationIds) {
        String message = buildCardsMessage(correlationId);
        messages.add(message);
    }
    
    return new MessageContainer(correlationIds, messages);
  }

  private String buildCardsMessage(String correlationId) {
        return """
        {
            "correlationId": "%s",
            "messageType": "PAYPOINT_REQUEST",
            "amount": 100.00,
            "currency": "GBP",
            "timestamp": "%s"
        }
        """.formatted(correlationId, System.currentTimeMillis());
  }

  private List<String> generateCorrelationIds(int count) {
    List<String> ids = new ArrayList<>();
    for (int i = 0; i < count; i++) {
          ids.add(UUID.randomUUID().toString());
    }
    return ids;
  }
}
