package ua.kpi.softeng_course.tictactoe.store.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public class SQLiteUserRepository implements UserRepository {

    private static final String FIND_BY_ID =
            "SELECT id, username FROM Users WHERE id = ?";

    private static final String FIND_BY_USERNAME =
            "SELECT id, username FROM Users WHERE username = ?";

    private static final RowMapper<UserDTO> USER_ROW_MAPPER =
            (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("username"));

    private final JdbcTemplate jdbc;

    public SQLiteUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<UserDTO> findById(int id) {
        return jdbc.query(FIND_BY_ID, USER_ROW_MAPPER, id)
                   .stream().findFirst();
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return jdbc.query(FIND_BY_USERNAME, USER_ROW_MAPPER, username)
                   .stream().findFirst();
    }
}
