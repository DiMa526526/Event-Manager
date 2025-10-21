package ru.Diana.dao;

import ru.Diana.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findByUsernameOrEmail(String username, String email);
}
