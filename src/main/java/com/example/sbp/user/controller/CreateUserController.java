package com.example.sbp.user.controller;

import com.example.sbp.common.web.ApiResponse;
import com.example.sbp.user.service.CreateUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateUserController {
    private final CreateUserService service;

    @PostMapping("/users/sign-up")
    ApiResponse<Void> create(@Valid @RequestBody Request request) {
        service.create(request.toCommand());
        return ApiResponse.success();
    }

    public record Request(
        @NotBlank(message = "이름은 필수 값입니다.")
        String name,
        @NotBlank(message = "비밀번호는 필수 값입니다.")
        String password
    ) {
        public CreateUserService.Command toCommand() {
            return new CreateUserService.Command(
                name,
                password
            );
        }
    }
}
