package com.example.sbp.user.vo;

import com.example.sbp.user.domain.User;

public record UserVo(
    long id,
    String name,
    User.RoleType role
) {
    public static UserVo from(User user) {
        return new UserVo(
            user.id(),
            user.name(),
            user.role()
        );
    }
}