package ua.kpi.softeng_course.tictactoe.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TicTacToeRoundImplTest {


    @Test
    void testRoundIdGenerated() {

        var roundIdGenerator = Mockito.mock(RoundIdGenerator.class);

        when(roundIdGenerator.nextRoundId()).thenReturn(123);

        var roundModel = new TicTacToeRoundImpl(roundIdGenerator);

        assertNull(roundModel.roundId());

        roundModel.start();

        assertNotNull(roundModel.roundId());

        assertEquals(123, roundModel.roundId());
    }

    @Test
    void testRoundStarts() {

        // arrange
        var roundIdGenerator = new RoundIdGenerator();
        var roundModel = new TicTacToeRoundImpl(roundIdGenerator);

        assertEquals(NextAction.Action.START, roundModel.nextAction().get().getAction());

        // act
        roundModel.start();

        // assert
        NextAction.Action expected = NextAction.Action.FINISH;
        assertEquals(expected, roundModel.nextAction().get().getAction());
    }

    @Test
    void testStartThrowsIfAlreadyStarted() {
        var roundIdGenerator = new RoundIdGenerator();
        var roundModel = new TicTacToeRoundImpl(roundIdGenerator);

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
        var roundIdGenerator = new RoundIdGenerator();
        var roundModel = new TicTacToeRoundImpl(roundIdGenerator);

        roundModel.start();

        assertEquals(NextAction.Action.FINISH, roundModel.nextAction().get().getAction());

        roundModel.finish();
    }

    @Test
    void testThrowsIfAlreadyFinished() {
        var roundIdGenerator = new RoundIdGenerator();
        var roundModel = new TicTacToeRoundImpl(roundIdGenerator);

        roundModel.start();

        roundModel.finish();

        assertThrows(IllegalStateException.class, roundModel::finish);
    }


    @Test
    void test() {
        assertEquals(1, 1);
    }

}