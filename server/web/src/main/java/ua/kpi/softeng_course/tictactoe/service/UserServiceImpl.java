package ua.kpi.softeng_course.tictactoe.service;

import org.springframework.stereotype.Service;
import ua.kpi.softeng_course.tictactoe.model.User;
import ua.kpi.softeng_course.tictactoe.store.user.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id)
                .map(dto -> new User(dto.username(), dto.id()));
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(dto -> new User(dto.username(), dto.id()));
    }
}