package com.example.sbp.user.service;

import com.example.sbp.common.exception.NotFoundException;
import com.example.sbp.user.repository.UserRepository;
import com.example.sbp.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sbp.common.support.ApplicationStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetUserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserVo get(long userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return UserVo.from(user);
    }
}