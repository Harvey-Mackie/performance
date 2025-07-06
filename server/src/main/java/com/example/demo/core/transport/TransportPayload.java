package com.example.demo.core.transport;

import java.util.List;

import lombok.Data;

@Data
public class TransportPayload {
    private final List<String> correlationIds;
    private final String messageData;
    
    public TransportPayload(List<String> correlationIds, String messageData) {
        this.correlationIds = correlationIds;
        this.messageData = messageData;
    }
    
    public static TransportPayload single(String correlationId, String messageData) {
        return new TransportPayload(List.of(correlationId), messageData);
    }
    
    public static TransportPayload batch(List<String> correlationIds, String messageData) {
        return new TransportPayload(correlationIds, messageData);
    }
  public List<String> getCorrelationIds() {
            return correlationIds;
        }
      
      public String getMessageData() {
            return messageData;
        }
}
