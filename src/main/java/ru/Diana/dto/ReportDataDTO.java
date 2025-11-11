package ru.Diana.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReportDataDTO {
    private LocalDateTime generatedAt;
    private long userCount;
    private long userCountTime;
    private List<EventDTO> events;
    private long eventsTime;
    private long totalTime;

    public ReportDataDTO() {}

    public ReportDataDTO(LocalDateTime generatedAt, long userCount, long userCountTime,
                         List<EventDTO> events, long eventsTime, long totalTime) {
        this.generatedAt = generatedAt;
        this.userCount = userCount;
        this.userCountTime = userCountTime;
        this.events = events;
        this.eventsTime = eventsTime;
        this.totalTime = totalTime;
    }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public long getUserCount() { return userCount; }
    public void setUserCount(long userCount) { this.userCount = userCount; }

    public long getUserCountTime() { return userCountTime; }
    public void setUserCountTime(long userCountTime) { this.userCountTime = userCountTime; }

    public List<EventDTO> getEvents() { return events; }
    public void setEvents(List<EventDTO> events) { this.events = events; }

    public long getEventsTime() { return eventsTime; }
    public void setEventsTime(long eventsTime) { this.eventsTime = eventsTime; }

    public long getTotalTime() { return totalTime; }
    public void setTotalTime(long totalTime) { this.totalTime = totalTime; }
}