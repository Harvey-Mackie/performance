package com.example.demo.core.messageBuilder;

import java.util.List;

import lombok.Data;

@Data
public class MessageContainer {
  private final List<String> correlationIds;
  private final List<String> messages; 

  public MessageContainer(List<String> correlationIds, List<String> messages) {
    this.correlationIds = correlationIds;
    this.messages = messages;
  }
     public List<String> getCorrelationIds() {
            return correlationIds;
        }
      
      public List<String> getMessages() {
            return messages;
        }
      
      public String getMessageForCorrelationId(String correlationId) {
            int index = correlationIds.indexOf(correlationId);
            return index >= 0 ? messages.get(index) : null;
        }
      
      public int size() {
            return correlationIds.size();
        }
      
      public boolean isEmpty() {
            return correlationIds.isEmpty();
        }
      
      public String getCorrelationId(int index) {
            return correlationIds.get(index);
        }
      
      public String getMessage(int index) {
            return messages.get(index);
        }
}
