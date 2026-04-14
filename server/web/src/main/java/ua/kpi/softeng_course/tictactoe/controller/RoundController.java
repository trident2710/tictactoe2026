package ua.kpi.softeng_course.tictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.kpi.softeng_course.tictactoe.api.*;
import ua.kpi.softeng_course.tictactoe.model.TicTacToeRoundImpl;
import ua.kpi.softeng_course.tictactoe.service.RoundIdGenerator;
import ua.kpi.softeng_course.tictactoe.store.RoundStore;
import ua.kpi.softeng_course.tictactoe.store.SessionStore;
import ua.kpi.softeng_course.tictactoe.view.TicTacToeRoundStateConverter;

@RestController
public class RoundController {

    private final RoundStore roundStore;
    private final RoundIdGenerator idGenerator;
    private final SessionStore sessionStore;

    @Autowired
    public RoundController(RoundStore roundStore,
                           RoundIdGenerator idGenerator,
                           SessionStore sessionStore) {
        this.roundStore = roundStore;
        this.idGenerator = idGenerator;
        this.sessionStore = sessionStore;
    }

    @PostMapping("/rounds")
    public ResponseEntity<CreateRoundResponse> createRound(@RequestBody CreateRoundRequest request) {
        System.out.println(request);
        var userId = sessionStore.getUserId(request.sessionId());

        return userId.map(id -> {
            var roundId = idGenerator.nextId();
            var round = new TicTacToeRoundImpl(roundId, id);
            round.join(id);
            roundStore.put(round);
            return ResponseEntity.ok(new CreateRoundResponse(TicTacToeRoundStateConverter.convert(round)));
        }).orElseGet(
                () -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }

    @PostMapping("/rounds/join")
    public ResponseEntity<JoinRoundResponse> joinRound(@RequestBody JoinRoundRequest request) {
        var userId = sessionStore.getUserId(request.sessionId());
        
        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        var round = roundStore.get(request.roundId());
        if (round.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        try {
            round.get().join(userId.get());
            return ResponseEntity.ok(new JoinRoundResponse(TicTacToeRoundStateConverter.convert(round.get())));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/rounds/start")
    public ResponseEntity<StartRoundResponse> startRound(@RequestBody StartRoundRequest request) {
        var userId = sessionStore.getUserId(request.sessionId());

        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var round = roundStore.get(request.roundId());
        if (round.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (round.get().ownerId() != userId.get()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            round.get().start();
            return ResponseEntity.ok(new StartRoundResponse(TicTacToeRoundStateConverter.convert(round.get())));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/rounds/finish")
    public ResponseEntity<FinishRoundResponse> finishRound(@RequestBody FinishRoundRequest request) {
        var userId = sessionStore.getUserId(request.sessionId());

        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var round = roundStore.get(request.roundId());
        if (round.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (round.get().ownerId() != userId.get()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            round.get().finish();
            return ResponseEntity.ok(new FinishRoundResponse(TicTacToeRoundStateConverter.convert(round.get())));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/rounds/move")
    public ResponseEntity<MakeMoveResponse> makeMove(@RequestBody MakeMoveRequest request) {
        var userId = sessionStore.getUserId(request.sessionId());

        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var round = roundStore.get(request.roundId());
        if (round.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            round.get().makeMove(userId.get(), request.row(), request.col());
            return ResponseEntity.ok(new MakeMoveResponse(TicTacToeRoundStateConverter.convert(round.get())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/rounds/{roundId}")
    public ResponseEntity<GetRoundStateResponse> getRoundState(@PathVariable("roundId") int roundId) {
        var round = roundStore.get(roundId);
        if (round.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(new GetRoundStateResponse(TicTacToeRoundStateConverter.convert(round.get())));
    }
} 