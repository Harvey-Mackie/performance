package com.example.demo.core.transport;

import java.util.Map;

public class TransportDestination {
    private final String target;
    private final Map<String, Object> properties;
    
    public TransportDestination(String target) {
        this.target = target;
        this.properties = Map.of();
    }
    
    public TransportDestination(String target, Map<String, Object> properties) {
        this.target = target;
        this.properties = properties;
    }
    
    public String getTarget() {
        return target;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
}
