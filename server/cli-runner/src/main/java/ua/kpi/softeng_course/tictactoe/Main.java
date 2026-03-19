package ua.kpi.softeng_course.tictactoe;

import ua.kpi.softeng_course.tictactoe.model.TicTacToeRoundImpl;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        var roundModel = new TicTacToeRoundImpl();

        System.out.println(roundModel.nextAction().orElseThrow().getAction());
    }
}