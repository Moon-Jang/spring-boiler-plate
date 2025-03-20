package com.example.sbp.user.controller;

import com.example.sbp.auth.AuthenticatedUser;
import com.example.sbp.common.web.ApiResponse;
import com.example.sbp.user.service.GetUserService;
import com.example.sbp.user.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetUserController {
    private final GetUserService service;

    @GetMapping("/users/me")
    @PreAuthorize("hasRole('USER')")
    ApiResponse<UserVo> get(@AuthenticationPrincipal AuthenticatedUser user) {
        return ApiResponse.success(
            service.get(user.id())
        );
    }
}