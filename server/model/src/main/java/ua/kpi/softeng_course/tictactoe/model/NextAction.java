package ua.kpi.softeng_course.tictactoe.model;

public class NextAction {

    private final Action action;

    public NextAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        START,
        MOVE_X,
        MOVE_O,
        FINISH
    }
}
