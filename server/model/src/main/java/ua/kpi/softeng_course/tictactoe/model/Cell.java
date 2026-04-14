package ua.kpi.softeng_course.tictactoe.model;

public record Cell(int row, int col, CellState state) {
    public Cell {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Invalid cell coordinates");
        }
    }
} 