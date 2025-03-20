package com.example.sbp.common.exception;

import com.example.sbp.common.web.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CommonExceptionHandler {
    public static final String USER_4XX_MESSAGE = "잘못된 요청입니다.";
    public static final String USER_5XX_MESSAGE = "예상치 못한 오류가 발생하였습니다. 관리자에게 문의해주세요";

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
    @ExceptionHandler(ConstraintViolationException.class)
    private ApiResponse<Void> constraintViolationException(ConstraintViolationException e,
                                                           HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
    })
    public ApiResponse<Void> illegalException(Exception e,
                                              HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        HttpRequestMethodNotSupportedException.class,
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        MultipartException.class,
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResponse<Void> badRequestException(BadRequestException e,
                                                 HttpServletRequest request) {
        log.warn("[{}] 잘못된 요청이 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.fail(
            e.status(),
            e.message()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<Void> notFoundException(NotFoundException e,
                                               HttpServletRequest request) {
        log.error("[{}] 해당 리소스는 존재하지 않습니다.", request.getRequestURI(), e);

        return ApiResponse.notFound(e.status(), e.message());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<Void> noResourceFoundException(NoResourceFoundException e,
                                                      HttpServletRequest request) {
        log.error("[{}] 해당 리소스는 존재하지 않습니다.", request.getRequestURI(), e);

        return ApiResponse.notFound();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalErrorException.class)
    public ApiResponse<Void> internalErrorException(InternalErrorException e,
                                                    HttpServletRequest request) {
        log.error("[{}] 서버 오류가 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.error(e.message());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public ApiResponse<Void> domainException(DomainException e,
                                             HttpServletRequest request) {
        log.warn("[{}] 도메인 오류가 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.fail(e.status(), e.message());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ApiResponse<Void> unauthorizedException(UnauthorizedException e,
                                                   HttpServletRequest request) {
        log.warn("[{}] 인증 오류가 발생하였습니다.", request.getRequestURI(), e);

        return ApiResponse.unauthorized(e.status(), e.message());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> accessDeniedException(AccessDeniedException e,
                                                   HttpServletRequest request) {
        log.warn("[{}] 접근 권한이 없습니다.", request.getRequestURI(), e);

        return ApiResponse.forbidden();
    }
}
