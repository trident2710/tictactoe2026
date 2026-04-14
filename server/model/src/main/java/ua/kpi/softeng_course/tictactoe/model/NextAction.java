package ua.kpi.softeng_course.tictactoe.model;

public class NextAction {

    private Action action;

    public NextAction() {
        this.action = null;
    }

    public NextAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        OCCUPY_SEATS,
        START,
        MOVE_X,
        MOVE_O,
        FINISH
    }
}
