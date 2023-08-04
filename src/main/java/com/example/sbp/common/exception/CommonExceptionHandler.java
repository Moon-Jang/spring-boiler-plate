package com.example.sbp.common.exception;

import com.example.sbp.common.web.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    public static final String USER_4XX_MESSAGE = "잘못된 요청입니다.";
    public static final String USER_5XX_MESSAGE = "예상치 못한 오류가 발생하였습니다. 관리자에게 문의해주세요";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApplicationException.class)
    public ApiResponse<Void> applicationException(ApplicationException e,
                                                  HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);
        return ApiResponse.error(
            e.status(),
            e.message()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Void> bindException(BindException e,
                                           HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);
        return ApiResponse.fail(
            e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("\n"))
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        HttpRequestMethodNotSupportedException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        MultipartException.class,
        ConstraintViolationException.class
    })
    public ApiResponse<Void> handle4xx(Exception e,
                                       HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);
        return ApiResponse.fail(USER_4XX_MESSAGE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handle5xx(Exception e,
                                       HttpServletRequest request) {
        log.error("[{}] 예상치 못한 오류가 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.error(USER_5XX_MESSAGE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerException.class)
    public ApiResponse<Void> serverException(ServerException e,
                                             HttpServletRequest request) {
        log.error("[{}] 서버 오류가 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.error(e.message());
    }
}
