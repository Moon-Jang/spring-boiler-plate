package com.example.sbp.user.domain;

import com.example.sbp.common.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserTest {

    @Nested
    @DisplayName("이름 검증 테스트")
    class ValidateNameTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setName("test1234")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isNull();
        }

        @Test
        void 이름_길이_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setName("11")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("이름은 4자 이상 10자 이하로 입력해주세요.");
        }

        @Test
        void 이름_패턴_검증_실패_케이스() {
            // given
            var user = new UserFixture()
                .setName("test!@#")
                .build();

            // when
            var throwable = catchThrowable(user::validateName);

            // then
            assertThat(throwable).isInstanceOf(DomainException.class)
                .hasMessageContaining("이름은 알파벳 소문자, 숫자로 구성되어야합니다.");
        }
    }

    @Nested
    @DisplayName("비밀번호 검증 테스트")
    class ValidatePasswordTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setPassword("test1234!")
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            assertThat(throwable).isNull();
        }

        @ParameterizedTest
        @MethodSource("passwordLengthArguments")
        void 비밀번호_길이_검증(String password, boolean expected) {
            // given
            var user = new UserFixture()
                .setPassword(password)
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            if (expected) {
                assertThat(throwable).isNull();
            } else {
                assertThat(throwable).isInstanceOf(DomainException.class)
                    .hasMessageContaining("비밀번호는 8자 이상 15자 이하로 입력해주세요.");
            }
        }

        static Stream<Arguments> passwordLengthArguments() {
            return Stream.of(
                Arguments.of("1".repeat(7), false),
                Arguments.of("1".repeat(16), false),
                Arguments.of("tet12345!", true),
                Arguments.of("test12345678^^", true)
            );
        }

        @ParameterizedTest
        @MethodSource("passwordPatternArguments")
        void 비밀번호_패턴_검증(String password, boolean expected) {
            // given
            var user = new UserFixture()
                .setPassword(password)
                .build();

            // when
            var throwable = catchThrowable(user::validatePassword);

            // then
            if (expected) {
                assertThat(throwable).isNull();
            } else {
                assertThat(throwable).isInstanceOf(DomainException.class)
                    .hasMessageContaining("비밀번호는 알파벳 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
            }
        }

        static Stream<Arguments> passwordPatternArguments() {
            return Stream.of(
                Arguments.of("test1234", false),
                Arguments.of("test!@#$", false),
                Arguments.of("12345678", false),
                Arguments.of("!1234567!", false),
                Arguments.of("test1234!", true),
                Arguments.of("1244*_15mm", true)
            );
        }
    }

    @Nested
    @DisplayName("비밀번호 일치 테스트")
    class MatchPasswordTest {
        @Test
        void success() {
            // given
            var user = new UserFixture()
                .setPassword("test1234!")
                .build();

            // when
            var isMatch = user.matchPassword(user.password());

            // then
            assertThat(isMatch).isTrue();
        }

        @Test
        void 비밀번호_불일치_케이스() {
            // given
            var user = new UserFixture()
                .setPassword("test1234!")
                .build();

            // when
            var isMatch = user.matchPassword("test12345!");

            // then
            assertThat(isMatch).isFalse();
        }
    }
}
