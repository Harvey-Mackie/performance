package com.example.demo.core.transport;

public interface TransportStrategy {
    void sendMessage(TransportDestination destination, TransportPayload payload);
    String getTransportType();
}
