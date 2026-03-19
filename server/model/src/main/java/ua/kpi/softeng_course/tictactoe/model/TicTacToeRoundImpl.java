package ua.kpi.softeng_course.tictactoe.model;

import java.util.Optional;

public class TicTacToeRoundImpl implements TicTacToeRound {

    private Status status = Status.NOT_STARTED;

    @Override
    public void start() {
        if (status != Status.NOT_STARTED) {
            throw new IllegalStateException("The round has already been started");
        } else {
            status = Status.ONGOING;
        }
    }

    @Override
    public void finish() {
        if (status != Status.ONGOING) {
            throw new IllegalStateException("The round is not active");
        } else {
            status = Status.FINISHED;
        }
    }

    @Override
    public Optional<NextAction> nextAction() {
        if (status == Status.NOT_STARTED) {
            return Optional.of(new NextAction(NextAction.Action.START));
        }
        if (status == Status.ONGOING) {
            return Optional.of(new NextAction(NextAction.Action.FINISH));
        }

        return Optional.empty();
    }

    enum Status {
        NOT_STARTED,
        ONGOING,
        FINISHED
    }
}
