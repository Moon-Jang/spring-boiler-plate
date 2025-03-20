package com.example.sbp.common.web;

import com.example.sbp.common.support.Status;

public record ApiResponse<T>(
    Status status,
    String message,
    T data
) {
    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            ApiResponseStatus.SUCCESS,
            ApiResponseStatus.SUCCESS.message(),
            data
        );
    }

    public static <T> ApiResponse<T> fail(Status status,
                                          String message) {
        return fail(
            status,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(String message) {
        return fail(
            ApiResponseStatus.FAILURE,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(Status status,
                                          String message,
                                          T data) {
        return new ApiResponse<>(
            status,
            message,
            data
        );
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(
            message,
            null
        );
    }

    public static <T> ApiResponse<T> error(Status status,
                                           String message) {
        return new ApiResponse<>(
            status,
            message,
            null
        );
    }

    public static <T> ApiResponse<T> error(String message,
                                           T errors) {
        return new ApiResponse<>(
            ApiResponseStatus.ERROR,
            message,
            errors
        );
    }

    public static <T> ApiResponse<T> custom(Status status,
                                            String message,
                                            T data) {
        return new ApiResponse<>(
            status,
            message,
            data
        );
    }

    public static ApiResponse<Void> unauthorized(Status status,
                                                 String message) {
        return new ApiResponse<>(
            status,
            message,
            null
        );
    }

    public static ApiResponse<Void> unauthorized(String message) {
        return unauthorized(
            ApiResponseStatus.UNAUTHORIZED,
            message
        );
    }

    public static ApiResponse<Void> unauthorized() {
        return unauthorized(
            ApiResponseStatus.UNAUTHORIZED.message()
        );
    }

    public static ApiResponse<Void> forbidden() {
        return new ApiResponse<>(
            ApiResponseStatus.FORBIDDEN,
            ApiResponseStatus.FORBIDDEN.message(),
            null
        );
    }

    public static ApiResponse<Void> notFound(Status status,
                                             String message) {
        return new ApiResponse<>(
            status,
            message,
            null
        );
    }

    public static ApiResponse<Void> notFound(String message) {
        return notFound(
            ApiResponseStatus.NOT_FOUND,
            message
        );
    }

    public static ApiResponse<Void> notFound() {
        return notFound(
            ApiResponseStatus.NOT_FOUND.message()
        );
    }

    enum ApiResponseStatus implements Status {
        SUCCESS("성공"),
        FAILURE( "요청에 실패하였습니다."),
        ERROR( "에러가 발생하였습니다."),
        UNAUTHORIZED("토큰이 유효하지 않습니다."),
        FORBIDDEN("권한이 없습니다."),
        NOT_FOUND("해당 리소스는 존재하지 않습니다.");

        private final String message;

        ApiResponseStatus(String message) {
            this.message = message;
        }

        @Override
        public String message() {
            return message;
        }
    }
}

