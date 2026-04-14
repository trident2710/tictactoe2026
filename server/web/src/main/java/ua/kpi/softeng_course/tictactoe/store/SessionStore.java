package ua.kpi.softeng_course.tictactoe.store;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionStore {
    private final Map<String, Integer> sessionToUserId = new HashMap<>();
    private final Map<Integer, String> userIdToSession = new HashMap<>();

    public String createSession(int userId) {
        // Remove existing session if any
        String existingSession = userIdToSession.remove(userId);
        if (existingSession != null) {
            sessionToUserId.remove(existingSession);
        }

        // Generate new session ID
        String sessionId = UUID.randomUUID().toString();
        
        // Store the mapping
        sessionToUserId.put(sessionId, userId);
        userIdToSession.put(userId, sessionId);
        
        return sessionId;
    }

    public Optional<Integer> getUserId(String sessionId) {
        return Optional.ofNullable(sessionToUserId.get(sessionId));
    }

    public Optional<String> getSessionId(int userId) {
        return Optional.ofNullable(userIdToSession.get(userId));
    }

    public void removeSession(String sessionId) {
        Integer userId = sessionToUserId.remove(sessionId);
        if (userId != null) {
            userIdToSession.remove(userId);
        }
    }

    public void removeSessionByUserId(int userId) {
        String sessionId = userIdToSession.remove(userId);
        if (sessionId != null) {
            sessionToUserId.remove(sessionId);
        }
    }

    public void clear() {
        sessionToUserId.clear();
        userIdToSession.clear();
    }
}
