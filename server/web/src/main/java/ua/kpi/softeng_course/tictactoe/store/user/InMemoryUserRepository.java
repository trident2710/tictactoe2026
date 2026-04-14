package ua.kpi.softeng_course.tictactoe.store.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    @Override
    public Optional<UserDTO> findById(int id) {
        return KnownUsers.KNOWN_USERS.values().stream()
                .filter(u -> u.id() == id)
                .map(u -> new UserDTO(u.id(), u.username()))
                .findFirst();
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return Optional.ofNullable(KnownUsers.KNOWN_USERS.get(username))
                .map(u -> new UserDTO(u.id(), u.username()));
    }
}
