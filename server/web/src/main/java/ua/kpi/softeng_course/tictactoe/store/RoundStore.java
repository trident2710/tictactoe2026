package ua.kpi.softeng_course.tictactoe.store;

import org.springframework.stereotype.Component;
import ua.kpi.softeng_course.tictactoe.model.TicTacToeRound;

import java.util.*;

@Component
public class RoundStore {

    private final Map<Integer, TicTacToeRound> store = new HashMap<>();

    public Optional<TicTacToeRound> get(int id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<TicTacToeRound> getAll() {
        return new ArrayList<>(store.values());
    }

    public void put(TicTacToeRound round) {
        store.put(round.roundId(), round);
    }

    public void clear() {
        store.clear();
    }
}
