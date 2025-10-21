package ru.Diana.service;

public interface EventManagementService {
    void createEventWithReminder(String eventTitle, String eventDescription,
                                 java.time.LocalDateTime eventTime,
                                 java.time.LocalDateTime remindTime,
                                 String username);
}
