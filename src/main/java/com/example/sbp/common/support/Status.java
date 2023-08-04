package com.example.sbp.common.support;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum Status {
    //common
    SUCCESS("성공"),
    ERROR("에러"),
    FAIL("요청을 처리할 수 없습니다."),
    NOT_FOUND("리소스를 찾을 수 없습니다."),
    UNAUTHORIZED("인증되지 않았습니다."),
    INVALID_PARAMETER("파라미터가 올바르지 않습니다."),
    EMPTY_TOKEN("토큰이 없습니다."),
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    NOT_ACCEPTABLE("허용하지 않는 요청입니다."),
    UNKNOWN("정의 되지 않은 상태입니다."),

    //order
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다.")
    ;

    private final String message;

    public static Status from(String text) {
        return Arrays.stream(Status.values())
            .filter(status -> status.name().equals(text))
            .findAny()
            .orElse(UNKNOWN);
    }
}

