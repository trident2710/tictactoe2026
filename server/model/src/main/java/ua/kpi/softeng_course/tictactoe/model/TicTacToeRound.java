package ua.kpi.softeng_course.tictactoe.model;

import java.util.Optional;

public interface TicTacToeRound {

    void start();

    void finish();

    Optional<NextAction> nextAction();
}
