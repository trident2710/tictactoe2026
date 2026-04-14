package ua.kpi.softeng_course.tictactoe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicTacToeRoundImpl implements TicTacToeRound {

    private final int roundId;
    private final int ownerId;
    private final List<Seat> seats = new ArrayList<>(2); // Array to store two players
    private final List<Cell> board = new ArrayList<>();
    private Status status = Status.NOT_STARTED;
    private PlayerType currentPlayerType = PlayerType.X;
    private Optional<GameResult> gameResult = Optional.empty();

    public TicTacToeRoundImpl(int roundId, int ownerId) {
        this.roundId = roundId;
        this.ownerId = ownerId;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board.add(new Cell(row, col, CellState.EMPTY));
            }
        }
    }

    @Override
    public int roundId() {
        return roundId;
    }

    @Override
    public int ownerId() {
        return ownerId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public List<Cell> getBoard() {
        return board;
    }

    @Override
    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public void start() {
        if (status != Status.NOT_STARTED) {
            throw new IllegalStateException("The round has already been started");
        }
        if (seats.size() < 2) {
            throw new IllegalStateException("Cannot start round: not all players have joined");
        }
        status = Status.ONGOING;
        currentPlayerType = PlayerType.X;
        gameResult = Optional.empty();
        // Clear the board
        board.clear();
        initializeBoard();
    }

    @Override
    public void finish() {
        if (nextAction().isEmpty() || nextAction().get().getAction() != NextAction.Action.FINISH) {
            throw new IllegalStateException("Round cannot be finished at this time");
        }
        status = Status.FINISHED;
    }

    @Override
    public Optional<NextAction> nextAction() {
        if (status == Status.NOT_STARTED) {
            if (seats.size() < 2) {
                return Optional.of(new NextAction(NextAction.Action.OCCUPY_SEATS));
            }
            return Optional.of(new NextAction(NextAction.Action.START));
        }
        if (status == Status.ONGOING) {
            var maybeGameResult = calculateGameResult();
            if (maybeGameResult.isPresent()) {
                gameResult = maybeGameResult;
                return Optional.of(new NextAction(NextAction.Action.FINISH));
            }
            if (currentPlayerType == PlayerType.X) {
                return Optional.of(new NextAction(NextAction.Action.MOVE_X));
            } else {
                return Optional.of(new NextAction(NextAction.Action.MOVE_O));
            }
        }
        return Optional.empty();
    }

    @Override
    public void makeMove(int playerId, int row, int col) {
        var action = nextAction();
        if (action.isEmpty() || (action.get().getAction() != NextAction.Action.MOVE_X && action.get().getAction() != NextAction.Action.MOVE_O)) {
            throw new IllegalStateException("Move is not allowed at this time");
        }

        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Invalid position");
        }

        var cell = board.stream()
                .filter(c -> c.row() == row && c.col() == col)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid position"));

        if (cell.state() != CellState.EMPTY) {
            throw new IllegalStateException("Position already taken");
        }

        // Check if it's the player's turn
        var playerSeat = getPlayerSeat(playerId)
                .orElseThrow(() -> new IllegalStateException("Player not in this round"));

        if (playerSeat.playerType() != currentPlayerType) {
            throw new IllegalStateException("Not your turn");
        }

        // Update the cell state
        board.remove(cell);
        board.add(new Cell(row, col, currentPlayerType == PlayerType.X ? CellState.X : CellState.O));
        currentPlayerType = (currentPlayerType == PlayerType.X) ? PlayerType.O : PlayerType.X;

        gameResult = calculateGameResult();
    }

    private Optional<GameResult> calculateGameResult() {
        // Check rows
        for (int row = 0; row < 3; row++) {
            final int finalRow = row;
            var rowCells = board.stream()
                    .filter(c -> c.row() == finalRow)
                    .collect(Collectors.toList());
            if (checkLine(rowCells)) {
                return Optional.of(rowCells.getFirst().state() == CellState.X ? GameResult.X_WINS : GameResult.O_WINS);
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            final int finalCol = col;
            var colCells = board.stream()
                    .filter(c -> c.col() == finalCol)
                    .collect(Collectors.toList());
            if (checkLine(colCells)) {
                return Optional.of(colCells.getFirst().state() == CellState.X ? GameResult.X_WINS : GameResult.O_WINS);
            }
        }

        // Check diagonals
        var diagonal1 = board.stream()
                .filter(c -> c.row() == c.col())
                .collect(Collectors.toList());
        if (checkLine(diagonal1)) {
            return Optional.of(diagonal1.getFirst().state() == CellState.X ? GameResult.X_WINS : GameResult.O_WINS);
        }

        var diagonal2 = board.stream()
                .filter(c -> c.row() + c.col() == 2)
                .collect(Collectors.toList());
        if (checkLine(diagonal2)) {
            return Optional.of(diagonal2.getFirst().state() == CellState.X ? GameResult.X_WINS : GameResult.O_WINS);
        }

        // Check for draw
        boolean isDraw = board.stream().noneMatch(c -> c.state() == CellState.EMPTY);
        if (isDraw) {
            return Optional.of(GameResult.DRAW);
        }

        return Optional.empty();
    }

    private boolean checkLine(List<Cell> cells) {
        if (cells.size() != 3) return false;
        var firstState = cells.getFirst().state();
        return firstState != CellState.EMPTY && cells.stream().allMatch(c -> c.state() == firstState);
    }

    @Override
    public Optional<GameResult> getGameResult() {
        return gameResult;
    }

    public void join(int playerId) {
        if (status != Status.NOT_STARTED) {
            throw new IllegalStateException("Cannot join a round that has already started");
        }
        if (seats.size() >= 2) {
            throw new IllegalStateException("Round is full");
        }
        if (seats.stream().map(Seat::playerId).toList().contains(playerId)) {
            throw new IllegalStateException("Player already joined");
        }

        // Assign player to first available seat
        if (seats.isEmpty()) {
            seats.add(new Seat(playerId, PlayerType.X));
        } else {
            seats.add(new Seat(playerId, PlayerType.O));
        }
    }

    private Optional<Seat> getPlayerSeat(int playerId) {
        return seats.stream().filter(c -> c.playerId() == playerId).findFirst();
    }
}
