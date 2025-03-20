package com.example.sbp.common.support;

public enum ApplicationStatus implements Status {
    INVALID_PARAMETER("잘못된 요청입니다."),
    UNAUTHORIZED("권한이 부족합니다."),

    // user
    USER_NOT_FOUND("회원을 찾을 수 없습니다."),
    ALREADY_EXIST_USER("이미 존재하는 사용자입니다."),
    ;

    private final String message;

    ApplicationStatus(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
