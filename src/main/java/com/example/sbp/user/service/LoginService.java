package com.example.sbp.user.service;

import com.example.sbp.common.component.JwtTokenManager;
import com.example.sbp.common.exception.BadRequestException;
import com.example.sbp.common.exception.NotFoundException;
import com.example.sbp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sbp.common.support.ApplicationStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    @Transactional(readOnly = true)
    public String login(Command command) {
        var user = userRepository.findByName(command.name())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        if (user.notMatchPassword(command.password())) {
            throw new BadRequestException(USER_NOT_FOUND);
        }

        return jwtTokenManager.issue(user.id(), user.name());
    }

    public record Command(
        String name,
        String password
    ) {
    }
} 