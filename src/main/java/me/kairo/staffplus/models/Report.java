package me.kairo.staffplus.models;

import java.util.UUID;

public class Report {
    
    private final UUID id;
    private final String reporter;
    private final String target;
    private final String reason;
    private final long timestamp;
    
    public Report(UUID id, String reporter, String target, String reason, long timestamp) {
        this.id = id;
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
        this.timestamp = timestamp;
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getReporter() {
        return reporter;
    }
    
    public String getTarget() {
        return target;
    }
    
    public String getReason() {
        return reason;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}