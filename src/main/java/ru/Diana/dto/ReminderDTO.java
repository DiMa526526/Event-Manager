package ru.Diana.dto;

import java.time.LocalDateTime;

public class ReminderDTO {
    private Long id;
    private LocalDateTime remindTime;
    private String method;
    private Boolean sent;
    private UserDTO user;
    private Long eventId;

    public ReminderDTO() {}

    public ReminderDTO(Long id, LocalDateTime remindTime, String method, Boolean sent,
                       UserDTO user, Long eventId) {
        this.id = id;
        this.remindTime = remindTime;
        this.method = method;
        this.sent = sent;
        this.user = user;
        this.eventId = eventId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getRemindTime() { return remindTime; }
    public void setRemindTime(LocalDateTime remindTime) { this.remindTime = remindTime; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Boolean getSent() { return sent; }
    public void setSent(Boolean sent) { this.sent = sent; }

    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}