package ru.Diana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import ru.Diana.entity.User;
import ru.Diana.entity.Report;
import ru.Diana.repository.UserRepository;
import ru.Diana.repository.ReportRepository;
import ru.Diana.service.ReportService;

import java.util.List;

@Tag(name = "User View", description = "Веб-интерфейс для просмотра пользователей и отчетов")
@Controller
public class UserViewController {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final ReportService reportService;

    public UserViewController(UserRepository userRepository,
                              ReportRepository reportRepository,
                              ReportService reportService) {
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
        this.reportService = reportService;
    }

    @Operation(
            summary = "Получить страницу со списком пользователей",
            description = "Возвращает HTML страницу со списком всех пользователей"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница успешно загружена")
    })
    @GetMapping("/users")
    public String getUsers(
            @Parameter(description = "Модель для передачи данных в представление")
            Model model) {
        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @Operation(
            summary = "Главная страница",
            description = "Возвращает главную страницу приложения"
    )
    @GetMapping("/")
    public String getHomePage() {
        return "user-list";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @Operation(
            summary = "Просмотреть отчет",
            description = "Отображает HTML отчет в браузере"
    )
    @GetMapping("/reports/{id}")
    public String viewReport(
            @Parameter(description = "ID отчета") @PathVariable Long id,
            Model model) {
        try {
            String content = reportService.getReportContent(id);
            model.addAttribute("reportContent", content);
            return "report-view";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Отчет не найден");
            return "error";
        }
    }

    @Operation(
            summary = "Создать новый отчет",
            description = "Создает новый отчет и перенаправляет на страницу просмотра"
    )
    @GetMapping("/reports/create")
    public String createReport(Model model) {
        try {
            Long reportId = reportService.createReportAsync().join();
            return "redirect:/reports/" + reportId;
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании отчета: " + e.getMessage());
            return "error";
        }
    }
}