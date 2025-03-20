package com.example.sbp.user.domain;

import com.example.sbp.common.entity.BaseEntity;
import com.example.sbp.common.exception.DomainException;
import com.example.sbp.common.support.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public User(String name,
                String password) {
        this.name = name;
        this.password = password;
        this.role = RoleType.USER;
        validateValues();
    }

    void validateValues() {
        validateName();
        validatePassword();
    }

    void validateName() {
        if (name == null || name.strip().isBlank()) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "이름은 필수 값입니다.");
        }

        if (name.length() < 4 || name.length() > 10) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "이름은 4자 이상 10자 이하로 입력해주세요.");
        }

        if (!name.matches("^[a-z0-9]*$")) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "이름은 알파벳 소문자, 숫자로 구성되어야합니다.");
        }
    }

    void validatePassword() {
        if (password == null || password.strip().isBlank()) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "비밀번호는 필수 값입니다.");
        }

        if (password.length() < 8 || password.length() > 15) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "비밀번호는 8자 이상 15자 이하로 입력해주세요.");
        }

        if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+\\\\|\\[\\]{};:'\",.<>/?]).*$")) {
            throw new DomainException(ExceptionStatus.INVALID_PARAMETER, "비밀번호는 알파벳 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
        }
    }

    public boolean matchPassword(String password) {
        return Objects.equals(this.password, password);
    }

    public boolean notMatchPassword(String password) {
        return !matchPassword(password);
    }

    public enum RoleType {
        ADMIN, USER
    }

    @Getter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    enum ExceptionStatus implements Status {
        INVALID_PARAMETER("잘못된 요청입니다.");

        private final String message;
    }
}
