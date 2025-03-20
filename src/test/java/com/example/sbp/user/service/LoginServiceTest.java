package com.example.sbp.user.service;

import com.example.sbp.common.component.JwtTokenManager;
import com.example.sbp.common.exception.BadRequestException;
import com.example.sbp.common.exception.NotFoundException;
import com.example.sbp.common.support.ApplicationStatus;
import com.example.sbp.user.domain.UserFixture;
import com.example.sbp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @InjectMocks
    private LoginService service;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenManager jwtTokenManager;

    @Test
    @DisplayName("로그인 성공 테스트")
    void success() {
        // given
        var request = new LoginService.Command("testuser", "password123");
        var user = new UserFixture()
            .setName(request.name())
            .setPassword(request.password())
            .build();

        given(userRepository.findByName(request.name()))
            .willReturn(Optional.of(user));
        given(jwtTokenManager.issue(user.id(), user.name()))
            .willReturn("jwtToken");

        // when
        var result = service.login(request);

        // then
        assertThat(result).isEqualTo("jwtToken");
    }

    @Test
    void 사용자를_찾을_수_없는_경우_실패() {
        // given
        var request = new LoginService.Command("nonexistent", "password123");

        given(userRepository.findByName(request.name()))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.login(request));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(ApplicationStatus.USER_NOT_FOUND.message());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는 경우 테스트")
    void 비밀번호가_일치하지_않는_경우_실패() {
        // given
        var request = new LoginService.Command("testuser", "wrongpassword");
        var user = new UserFixture()
            .setName(request.name())
            .setPassword("password123")
            .build();

        given(userRepository.findByName(request.name()))
            .willReturn(Optional.of(user));

        // when
        var throwable = catchThrowable(() -> service.login(request));

        // then
        assertThat(throwable).isInstanceOf(BadRequestException.class)
            .hasMessage(ApplicationStatus.USER_NOT_FOUND.message());
    }
} 