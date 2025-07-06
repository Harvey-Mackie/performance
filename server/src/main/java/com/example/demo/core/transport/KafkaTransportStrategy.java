package com.example.demo.core.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransportStrategy implements TransportStrategy {
    private static final Logger log = LoggerFactory.getLogger(KafkaTransportStrategy.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public KafkaTransportStrategy(KafkaTemplate<String, String> kafkaTemplate) {
          this.kafkaTemplate = kafkaTemplate;
      }
    
    @Override
        public void sendMessage(TransportDestination destination, TransportPayload payload) {
          String topic = destination.getTarget();
          
          for (String correlationId : payload.getCorrelationIds()) {
                log.debug("Sending Kafka message to topic: {} with correlationId: {}", topic, correlationId);
                kafkaTemplate.send(topic, correlationId, payload.getMessageData());
            }
      }
    
    @Override
        public String getTransportType() {
          return "KAFKA";
      }
}
