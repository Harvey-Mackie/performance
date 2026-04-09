package com.example.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "")
public class ApplicationProperties {
    private String environment = "local";
    private Domains domains = new Domains();
    private Kafka kafka = new Kafka();

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Domains getDomains() {
        return domains;
    }

    public void setDomains(Domains domains) {
        this.domains = domains;
    }

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public int getCardsTimeout() {
        return domains.cards.timeout;
    }

    public int getCardsPollInterval() {
        return domains.cards.pollInterval;
    }

    public int getCardsMessageCount() {
        return domains.cards.messageCount;
    }

    public String getCardsRequestTopic() {
        return kafka.topics.cardRequest;
    }

    public String getCardsResponseTopic() {
        return kafka.topics.cardResponse;
    }

    public static class Domains {
        private Cards cards = new Cards();

        public Cards getCards() {
            return cards;
        }

        public void setCards(Cards cards) {
            this.cards = cards;
        }
    }

    public static class Cards {
        private int timeout = 5;
        private int pollInterval = 2;
        private int messageCount = 10;

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public int getPollInterval() {
            return pollInterval;
        }

        public void setPollInterval(int pollInterval) {
            this.pollInterval = pollInterval;
        }

        public int getMessageCount() {
            return messageCount;
        }

        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }
    }

    public static class Kafka {
        private Topics topics = new Topics();

        public Topics getTopics() {
            return topics;
        }

        public void setTopics(Topics topics) {
            this.topics = topics;
        }
    }

    public static class Topics {
        private String cardRequest = "request-topic";
        private String cardResponse = "posting-request-topic";

        public String getCardRequest() {
            return cardRequest;
        }

        public void setCardRequest(String cardRequest) {
            this.cardRequest = cardRequest;
        }

        public String getCardResponse() {
            return cardResponse;
        }

        public void setCardResponse(String cardResponse) {
            this.cardResponse = cardResponse;
        }
    }
}
