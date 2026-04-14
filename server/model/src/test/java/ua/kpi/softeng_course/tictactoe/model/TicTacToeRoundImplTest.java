package ua.kpi.softeng_course.tictactoe.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeRoundImplTest {

    private static final int PLAYER_X_ID = 1;
    private static final int PLAYER_O_ID = 2;
    private static final int ROUND_ID = 42;
    private static final int OWNER_ID = 100;

    @Test
    void testRoundHasOwner() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        assertEquals(OWNER_ID, roundModel.ownerId());
    }

    @Test
    void testRoundStarts() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);

        assertEquals(NextAction.Action.START, roundModel.nextAction().get().getAction());
        roundModel.start();

        assertEquals(NextAction.Action.MOVE_X, roundModel.nextAction().get().getAction());
    }

    @Test
    void testStartThrowsIfAlreadyStarted() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);

        assertEquals(NextAction.Action.START, roundModel.nextAction().get().getAction());

        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                roundModel.start();
            }
        });

        assertThrows(IllegalStateException.class, roundModel::start);
    }

    @Test
    void testFinishes() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        // Play moves until X wins
        roundModel.makeMove(PLAYER_X_ID, 0, 0); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 0); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 1); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 1); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 2); // X wins

        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());
        roundModel.finish();
        
        // Verify next action is empty after finishing
        assertEquals(Optional.empty(), roundModel.nextAction());
    }

    @Test
    void testThrowsIfAlreadyFinished() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();
        roundModel.makeMove(PLAYER_X_ID, 0, 0);
        roundModel.makeMove(PLAYER_O_ID, 1, 0);
        roundModel.makeMove(PLAYER_X_ID, 0, 1);
        roundModel.makeMove(PLAYER_O_ID, 1, 1);
        roundModel.makeMove(PLAYER_X_ID, 0, 2); // X wins
        roundModel.finish();

        assertThrows(IllegalStateException.class, roundModel::finish);
    }

    @Test
    void testPlayerTurnsAlternate() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        // First turn should be X
        assertEquals(NextAction.Action.MOVE_X, roundModel.nextAction().get().getAction());
        
        // After X moves, it should be O's turn
        roundModel.makeMove(PLAYER_X_ID, 0, 0);
        assertEquals(NextAction.Action.MOVE_O, roundModel.nextAction().get().getAction());
        
        // After O moves, it should be X's turn again
        roundModel.makeMove(PLAYER_O_ID, 0, 1);
        assertEquals(NextAction.Action.MOVE_X, roundModel.nextAction().get().getAction());
    }

    @Test
    void testCannotMakeMoveBeforeGameStarts() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);

        assertThrows(IllegalStateException.class, () -> roundModel.makeMove(PLAYER_X_ID, 0, 0));
    }

    @Test
    void testCannotMakeMoveAfterGameFinishes() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();
        roundModel.makeMove(PLAYER_X_ID, 0, 0);
        roundModel.makeMove(PLAYER_O_ID, 1, 0);
        roundModel.makeMove(PLAYER_X_ID, 0, 1);
        roundModel.makeMove(PLAYER_O_ID, 1, 1);
        roundModel.makeMove(PLAYER_X_ID, 0, 2); // X wins
        roundModel.finish();

        assertThrows(IllegalStateException.class, () -> roundModel.makeMove(PLAYER_X_ID, 0, 0));
    }

    @Test
    void testGameStartsWithX() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        assertEquals(NextAction.Action.MOVE_X, roundModel.nextAction().get().getAction());
    }

    @Test
    void testCannotMakeMoveToInvalidPosition() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        assertThrows(IllegalArgumentException.class, () -> roundModel.makeMove(PLAYER_X_ID, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> roundModel.makeMove(PLAYER_X_ID, 3, 0));
        assertThrows(IllegalArgumentException.class, () -> roundModel.makeMove(PLAYER_X_ID, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> roundModel.makeMove(PLAYER_X_ID, 0, 3));
    }

    @Test
    void testCannotMakeMoveToOccupiedPosition() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();
        roundModel.makeMove(PLAYER_X_ID, 0, 0);

        assertThrows(IllegalStateException.class, () -> roundModel.makeMove(PLAYER_O_ID, 0, 0));
    }

    @Test
    void testGameFinishesWhenXWins() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        // X wins horizontally
        roundModel.makeMove(PLAYER_X_ID, 0, 0); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 0); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 1); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 1); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 2); // X

        assertEquals(Optional.of(GameResult.X_WINS), roundModel.getGameResult());
        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());
    }

    @Test
    void testGameFinishesWhenOWins() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        // O wins vertically
        roundModel.makeMove(PLAYER_X_ID, 0, 0); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 0); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 1); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 1); // O
        roundModel.makeMove(PLAYER_X_ID, 2, 2); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 2); // O

        assertEquals(Optional.of(GameResult.O_WINS), roundModel.getGameResult());
        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());
    }

    @Test
    void testGameFinishesWithDraw() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        // Create a draw scenario
        roundModel.makeMove(PLAYER_X_ID, 0, 0); // X
        roundModel.makeMove(PLAYER_O_ID, 0, 1); // O
        roundModel.makeMove(PLAYER_X_ID, 0, 2); // X
        roundModel.makeMove(PLAYER_O_ID, 1, 0); // O
        roundModel.makeMove(PLAYER_X_ID, 1, 1); // X
        roundModel.makeMove(PLAYER_O_ID, 2, 0); // O
        roundModel.makeMove(PLAYER_X_ID, 1, 2); // X
        roundModel.makeMove(PLAYER_O_ID, 2, 2); // O
        roundModel.makeMove(PLAYER_X_ID, 2, 1); // X

        assertEquals(Optional.of(GameResult.DRAW), roundModel.getGameResult());
        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());
    }

    @Test
    void testCannotMakeMoveIfNotJoined() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        assertThrows(IllegalStateException.class, () -> roundModel.makeMove(999, 0, 0));
    }

    @Test
    void testCannotMakeMoveOutOfTurn() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        assertThrows(IllegalStateException.class, () -> roundModel.makeMove(PLAYER_O_ID, 0, 0));
    }

    @Test
    void testCannotStartWithoutBothPlayers() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);

        // Try to start with no players
        assertThrows(IllegalStateException.class, roundModel::start);

        // Try to start with only one player
        roundModel.join(PLAYER_X_ID);
        assertThrows(IllegalStateException.class, roundModel::start);

        // Should be able to start with both players
        roundModel.join(PLAYER_O_ID);
        assertDoesNotThrow(roundModel::start);
    }

    @Test
    void testGameResultIsEmptyWhenInProgress() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        assertEquals(Optional.empty(), roundModel.getGameResult());
    }

    @Test
    void testBoardIsInitializedWithEmptyCells() {
        var roundModel = new TicTacToeRoundImpl(ROUND_ID, OWNER_ID);
        roundModel.join(PLAYER_X_ID);
        roundModel.join(PLAYER_O_ID);
        roundModel.start();

        var board = roundModel.getBoard();
        board.forEach(cell -> assertEquals(CellState.EMPTY, cell.state()));
    }
}