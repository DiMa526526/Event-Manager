package ru.Diana.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime remindTime;

    @Column
    private String method = "PUSH";

    @Column
    private Boolean sent = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "reminder", cascade = CascadeType.ALL)
    private List<NotificationLog> notificationLogs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(LocalDateTime remindTime) {
        this.remindTime = remindTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<NotificationLog> getNotificationLogs() {
        return notificationLogs;
    }

    public void setNotificationLogs(List<NotificationLog> notificationLogs) {
        this.notificationLogs = notificationLogs;
    }
}
