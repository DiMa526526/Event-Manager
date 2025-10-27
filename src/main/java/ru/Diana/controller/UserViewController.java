package ru.Diana.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Hidden;

import ru.Diana.entity.User;
import ru.Diana.repository.UserRepository;

import java.util.List;

@Tag(name = "User View", description = "Веб-интерфейс для просмотра пользователей")
@Controller
public class UserViewController {

    private final UserRepository userRepository;

    public UserViewController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}