package com.example.demo.domains.cards;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.demo.core.messageBuilder.MessageContainer;
import com.example.demo.core.transport.KafkaTransportStrategy;
import com.example.demo.core.transport.TransportDestination;
import com.example.demo.core.transport.TransportPayload;
import com.example.demo.properties.ApplicationProperties;

@Component
public class CardsService {
  private final ApplicationProperties applicationProperties;
  private final CardsMessageBuilder cardsMessageBuilder;
  private final KafkaTransportStrategy kafkaTransportStrategy;

  private volatile boolean testInProgress = false;

  public CardsService(ApplicationProperties applicationProperties, CardsMessageBuilder cardsMessageBuilder, KafkaTransportStrategy kafkaTransportStrategy) {
    this.cardsMessageBuilder = cardsMessageBuilder;
    this.applicationProperties = applicationProperties;
    this.kafkaTransportStrategy = kafkaTransportStrategy;
  }

  public void loadTest(){
    //mark test as in progress
    if (testInProgress) {
      throw new RuntimeException("Test is already in progress");
    }
    testInProgress = true;

    //build messages
    var cardMessages = cardsMessageBuilder.buildCardsMessages(); 

    //creating tracking table
    Map<String, Long> cardRequests = new ConcurrentHashMap<>();

    //produce messages 
    var cardTask = sendMessagesAsync(cardMessages, applicationProperties.getKafkaTopicsCardRequest(), cardRequests);
    CompletableFuture.allOf(cardTask).join(); 
    
    //wait on responses 

    //calculate metrics
  }

  private CompletableFuture<Void> sendMessagesAsync(MessageContainer messageContainer, 
                                                    String topic, 
                                                    Map<String, Long> requestTracker) {
      return CompletableFuture.runAsync(() -> {
            TransportDestination destination = new TransportDestination(topic);
            
            var correlationIds = messageContainer.getCorrelationIds();
            var messages = messageContainer.getMessages();
            
            for (int i = 0; i < correlationIds.size(); i++) {
                  var correlationId = correlationIds.get(i); String
                  message = messages.get(i);
                  
                  TransportPayload payload = TransportPayload.single(correlationId, message);
                  kafkaTransportStrategy.sendMessage(destination, payload);
                        
                        // Record request timestamp
        requestTracker.put(correlationId, System.currentTimeMillis());
      }
    });
  }
}
