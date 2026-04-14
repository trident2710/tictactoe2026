package ua.kpi.softeng_course.tictactoe.store.user;

import java.util.Optional;

public interface UserRepository {

    Optional<UserDTO> findById(int id);

    Optional<UserDTO> findByUsername(String username);
}
