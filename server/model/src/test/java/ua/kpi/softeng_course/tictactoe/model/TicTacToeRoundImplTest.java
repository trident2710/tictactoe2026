package ua.kpi.softeng_course.tictactoe.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeRoundImplTest {

    @Test
    void testRoundStarts() {

        // arrange
        var roundModel = new TicTacToeRoundImpl();

        assertEquals(NextAction.Action.START, roundModel.nextAction().get().getAction());

        // act
        roundModel.start();

        // assert
        NextAction.Action expected = NextAction.Action.FINISH;
        assertEquals(expected, roundModel.nextAction().get().getAction());
    }

    @Test
    void testStartThrowsIfAlreadyStarted() {
        var roundModel = new TicTacToeRoundImpl();

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
        var roundModel = new TicTacToeRoundImpl();

        roundModel.start();

        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());

        roundModel.finish();
    }

    @Test
    void testThrowsIfAlreadyFinished() {
        var roundModel = new TicTacToeRoundImpl();

        roundModel.start();

        roundModel.finish();

        assertThrows(IllegalStateException.class, roundModel::finish);
    }


    @Test
    void test() {
        assertEquals(1, 1);
    }

}