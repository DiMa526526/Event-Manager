package ru.Diana.model;

import java.time.LocalDateTime;

public class Event {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventTime;
    private boolean notified;

    public Event() {
    }

    public Event(Long id, String title, String description, LocalDateTime eventTime, boolean notified) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventTime = eventTime;
        this.notified = notified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventTime=" + eventTime +
                ", notified=" + notified +
                '}';
    }
}
