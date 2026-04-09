package com.example.demo.domains.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.core.messageBuilder.MessageContainer;

@Component
public class CardsMessageBuilder {
    public MessageContainer buildCardsMessages(int count) {
        List<String> correlationIds = generateCorrelationIds(count);
        List<String> messages = new ArrayList<>();

        for (String correlationId : correlationIds) {
            messages.add(buildCardsMessage(correlationId));
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
        for (int index = 0; index < count; index++) {
            ids.add(UUID.randomUUID().toString());
        }
        return ids;
    }
}
