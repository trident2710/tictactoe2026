package ua.kpi.softeng_course.tictactoe.model;

import java.util.Random;

public class RoundIdGenerator {

    public int nextRoundId() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }
}
