package com.example.sbp.user.service;

import com.example.sbp.common.exception.BadRequestException;
import com.example.sbp.user.domain.User;
import com.example.sbp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {
    @InjectMocks
    private CreateUserService service;
    @Mock
    private UserRepository userRepository;

    @Test
    void success() {
        // given
        var command = new CreateUserService.Command(
            "test1234",
            "password123*"
        );
        given(userRepository.existsByName(command.name())).willReturn(false);

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isNull();
        var userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        var captured = userCaptor.getValue();
        assertThat(captured.name()).isEqualTo(command.name());
        assertThat(captured.password()).isEqualTo(command.password());
    }
    
    @Test
    void 이미_존재하는_이름일_경우_실패() {
        // given
        var command = new CreateUserService.Command(
            "test1234",
            "비밀번호"
        );
        given(userRepository.existsByName(command.name())).willReturn(true);

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isInstanceOf(BadRequestException.class)
            .hasMessageContaining("중복된 username 입니다.");
    }
}