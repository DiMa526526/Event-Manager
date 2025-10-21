package ru.Diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Diana.entity.Event;
import ru.Diana.entity.Reminder;
import ru.Diana.entity.User;
import ru.Diana.repository.EventRepository;
import ru.Diana.repository.ReminderRepository;
import ru.Diana.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class EventManagementServiceImpl implements EventManagementService {

    private final EventRepository eventRepository;
    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;

    @Autowired
    public EventManagementServiceImpl(EventRepository eventRepository,
                                      ReminderRepository reminderRepository,
                                      UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEventWithReminder(String eventTitle, String eventDescription,
                                        LocalDateTime eventTime, LocalDateTime remindTime,
                                        String username) {

        if (remindTime.isAfter(eventTime)) {
            throw new RuntimeException("Ошибка: откат транзакции. Время напоминания не может быть позже времени события");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));

        Event event = new Event();
        event.setTitle(eventTitle);
        event.setDescription(eventDescription);
        event.setEventTime(eventTime);
        event.setNotified(false);
        event.setUser(user);

        Event savedEvent = eventRepository.save(event);
        System.out.println("Событие создано: " + eventTitle);

        Reminder reminder = new Reminder();
        reminder.setRemindTime(remindTime);
        reminder.setMethod("PUSH");
        reminder.setSent(false);
        reminder.setUser(user);
        reminder.setEvent(savedEvent);

        reminderRepository.save(reminder);
        System.out.println("Напоминание создано для события: " + eventTitle);
        System.out.println("Транзакция успешно завершена");
    }
}