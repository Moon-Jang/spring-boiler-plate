package com.example.sbp.user.service;

import com.example.sbp.common.exception.BadRequestException;
import com.example.sbp.user.domain.User;
import com.example.sbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sbp.common.support.ApplicationStatus.ALREADY_EXIST_USER;

@Service
@RequiredArgsConstructor
public class CreateUserService {
    private final UserRepository userRepository;

    @Transactional
    public void create(Command command) {
        if (userRepository.existsByName(command.name())) {
            throw new BadRequestException(ALREADY_EXIST_USER, "중복된 username 입니다.");
        }

        var user = createUser(command);

        userRepository.save(user);
    }

    private User createUser(Command command) {
        return new User(
            command.name(),
            command.password()
        );
    }

    public record Command(
        String name,
        String password
    ) {
    }
}
