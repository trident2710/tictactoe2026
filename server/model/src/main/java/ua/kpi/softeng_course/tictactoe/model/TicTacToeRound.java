package ua.kpi.softeng_course.tictactoe.model;

import java.util.List;
import java.util.Optional;

public interface TicTacToeRound {

    int roundId();

    int ownerId();

    void start();

    Optional<NextAction> nextAction();

    void finish();

    void makeMove(int playerId, int row, int col);

    Optional<GameResult> getGameResult();

    void join(int playerId);

    List<Seat> getSeats();

    Status getStatus();

    List<Cell> getBoard();

}
