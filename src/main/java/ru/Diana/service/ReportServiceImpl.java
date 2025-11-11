package ru.Diana.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.Diana.dto.EventDTO;
import ru.Diana.dto.ReportDataDTO;
import ru.Diana.dto.UserDTO;
import ru.Diana.entity.*;
import ru.Diana.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TemplateEngine templateEngine;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository,
                             UserRepository userRepository,
                             EventRepository eventRepository,
                             TemplateEngine templateEngine) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.templateEngine = templateEngine;
    }

    @Override
    public Long createReport() {
        Report report = new Report(ReportStatus.CREATED);
        report.setReportType("STATISTICS");
        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }

    @Override
    public String getReportContent(Long reportId) {
        return reportRepository.findById(reportId)
                .map(Report::getContent)
                .orElseThrow(() -> new RuntimeException("Отчет не найден с ID: " + reportId));
    }

    @Override
    public CompletableFuture<Long> createReportAsync() {
        return CompletableFuture.supplyAsync(() -> {
            Long reportId = createReport();
            long overallStartTime = System.currentTimeMillis();

            try {
                CompletableFuture<ReportData> userCountFuture = CompletableFuture.supplyAsync(() -> {
                    long startTime = System.currentTimeMillis();
                    long userCount = getUserCount();
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    return new ReportData(userCount, elapsedTime);
                });

                CompletableFuture<ReportData> eventsFuture = CompletableFuture.supplyAsync(() -> {
                    long startTime = System.currentTimeMillis();
                    List<Event> events = getRecentEvents();
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    return new ReportData(events, elapsedTime);
                });

                ReportData userCountData = userCountFuture.join();
                ReportData eventsData = eventsFuture.join();

                long totalTime = System.currentTimeMillis() - overallStartTime;

                List<EventDTO> eventDTOs = ((List<Event>) eventsData.getData()).stream()
                        .map(this::convertToEventDTO)
                        .collect(Collectors.toList());

                ReportDataDTO reportData = new ReportDataDTO();
                reportData.setGeneratedAt(LocalDateTime.now());
                reportData.setUserCount((Long) userCountData.getData());
                reportData.setUserCountTime(userCountData.getElapsedTime());
                reportData.setEvents(eventDTOs);
                reportData.setEventsTime(eventsData.getElapsedTime());
                reportData.setTotalTime(totalTime);

                String htmlContent = generateHtmlWithTemplate(reportData);

                updateReport(reportId, ReportStatus.COMPLETE, htmlContent);

                return reportId;

            } catch (Exception e) {
                updateReport(reportId, ReportStatus.ERROR, "Ошибка при формировании отчета: " + e.getMessage());
                throw new RuntimeException("Ошибка генерации отчета", e);
            }
        });
    }

    private EventDTO convertToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setTitle(event.getTitle());
        eventDTO.setDescription(event.getDescription());
        eventDTO.setEventTime(event.getEventTime());
        eventDTO.setNotified(event.getNotified());

        if (event.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(event.getUser().getId());
            userDTO.setUsername(event.getUser().getUsername());
            userDTO.setEmail(event.getUser().getEmail());
            eventDTO.setUser(userDTO);
        }

        if (event.getCategory() != null) {
            eventDTO.setCategoryId(event.getCategory().getId());
        }

        return eventDTO;
    }

    private String generateHtmlWithTemplate(ReportDataDTO reportData) {
        Context context = new Context();
        context.setVariable("generatedAt", reportData.getGeneratedAt());
        context.setVariable("userCount", reportData.getUserCount());
        context.setVariable("userCountTime", reportData.getUserCountTime());
        context.setVariable("events", reportData.getEvents());
        context.setVariable("eventsTime", reportData.getEventsTime());
        context.setVariable("totalTime", reportData.getTotalTime());

        return templateEngine.process("report", context);
    }

    private void updateReport(Long reportId, ReportStatus status, String content) {
        reportRepository.findById(reportId).ifPresent(report -> {
            report.setStatus(status);
            report.setContent(content);
            report.setCompletedAt(LocalDateTime.now());
            reportRepository.save(report);
        });
    }

    private long getUserCount() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return userRepository.count();
    }

    private List<Event> getRecentEvents() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return ((List<Event>) eventRepository.findAll()).stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    private static class ReportData {
        private final Object data;
        private final long elapsedTime;

        public ReportData(Object data, long elapsedTime) {
            this.data = data;
            this.elapsedTime = elapsedTime;
        }

        public Object getData() { return data; }
        public long getElapsedTime() { return elapsedTime; }
    }
}