package com.example.sbp.user.controller;

import com.example.sbp.common.web.ApiResponse;
import com.example.sbp.user.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService service;

    @PostMapping("/users/login")
    ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody Request request) {
        var issuedToken = service.login(request.toCommand());
        
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + issuedToken);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success());
    }

    public record Request(
        String name,
        String password
    ) {
        public LoginService.Command toCommand() {
            return new LoginService.Command(
                name,
                password
            );
        }
    }
} 