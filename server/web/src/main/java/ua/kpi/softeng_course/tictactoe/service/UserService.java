package ua.kpi.softeng_course.tictactoe.service;

import ua.kpi.softeng_course.tictactoe.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserById(int id);

    Optional<User> findUserByUsername(String username);
} 