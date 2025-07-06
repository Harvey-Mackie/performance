package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "")
public class ApplicationProperties {

    // Domains - Cards
    private int domainsCardsTimeout;       // maps to domains.cards.timeout
    private int domainsCardsPollInterval;  // maps to domains.cards.pollInterval

    // Kafka Topics
    private String kafkaTopicsCardRequest;    // maps to kafka.topics.cardRequest
    private String kafkaTopicsCardResponse;   // maps to kafka.topics.cardResponse
}

