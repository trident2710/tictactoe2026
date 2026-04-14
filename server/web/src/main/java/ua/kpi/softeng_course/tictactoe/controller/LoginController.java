package ua.kpi.softeng_course.tictactoe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.softeng_course.tictactoe.api.LoginRequest;
import ua.kpi.softeng_course.tictactoe.api.LoginResponse;
import ua.kpi.softeng_course.tictactoe.model.User;
import ua.kpi.softeng_course.tictactoe.service.UserServiceImpl;
import ua.kpi.softeng_course.tictactoe.store.SessionStore;

@RestController
public class LoginController {
    private final SessionStore sessionStore;
    private final UserServiceImpl userService;

    @Autowired
    public LoginController(SessionStore sessionStore, UserServiceImpl userService) {
        this.sessionStore = sessionStore;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);

        var maybeUser = userService.findUserByUsername(loginRequest.username());
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = maybeUser.get();
        String sessionId = sessionStore.createSession(user.id());
        return ResponseEntity.ok(new LoginResponse(user, sessionId));
    }
}
