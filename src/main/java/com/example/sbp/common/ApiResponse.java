package com.example.sbp.common;

import java.time.ZonedDateTime;

public class ApiResponse<T> {
    private Status status;
    private String message;
    private ZonedDateTime serverDatetime;
    private T data;

    public ApiResponse() {
    }

    private ApiResponse(Status status,
                        String message,
                        T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.serverDatetime = ZonedDateTime.now();
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(
            Status.SUCCESS,
            Status.SUCCESS.message(),
            data
        );
    }

    public static <T> ApiResponse<T> fail(String message) {
        return fail(
            message,
            null
        );
    }

    public static <T> ApiResponse<T> fail(String message,
                                          T data) {
        return new ApiResponse<T>(
            Status.FAIL,
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

    public static <T> ApiResponse<T> error(String message,
                                           T errors) {
        return new ApiResponse<T>(
            Status.ERROR,
            message,
            errors
        );
    }

    public static <T> ApiResponse<T> custom(Status status,
                                            T data) {
        return custom(
            status,
            status.message(),
            data
        );
    }

    public static <T> ApiResponse<T> custom(Status status,
                                            String message) {
        return custom(
            status,
            message,
            null
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

    public T data() {
        return data;
    }
}

