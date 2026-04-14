package ua.kpi.softeng_course.tictactoe.api;

import ua.kpi.softeng_course.tictactoe.model.User;

public record LoginResponse(User user, String sessionId) {
}
