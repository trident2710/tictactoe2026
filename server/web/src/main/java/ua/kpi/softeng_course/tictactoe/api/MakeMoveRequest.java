package ua.kpi.softeng_course.tictactoe.api;

public record MakeMoveRequest(String sessionId, int roundId, int row, int col) {
}