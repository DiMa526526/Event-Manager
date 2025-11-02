package ru.Diana.dto;

import java.time.LocalDateTime;

public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventTime;
    private Boolean notified;
    private UserDTO user;
    private Long categoryId;

    public EventDTO() {}

    public EventDTO(Long id, String title, String description, LocalDateTime eventTime,
                    Boolean notified, UserDTO user, Long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventTime = eventTime;
        this.notified = notified;
        this.user = user;
        this.categoryId = categoryId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public Boolean getNotified() { return notified; }
    public void setNotified(Boolean notified) { this.notified = notified; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}