package com.example.sbp.user.domain;

import com.example.sbp.common.FixtureReflectionUtils;
import com.example.sbp.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class UserFixture implements TestFixture<User> {
    private long id = 1L;
    private String name = "testUser99";
    private String password = "test1234!";
    private User.RoleType role = User.RoleType.USER;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    @Override
    public User build() {
        var entity = new User();
        FixtureReflectionUtils.reflect(entity, this);
        return entity;
    }

    public static User normalUser() {
        return new UserFixture().build();
    }

    public static User adminUser() {
        return new UserFixture()
            .setId(2L)
            .setName("testAdmin1")
            .setPassword("adminPassword12*")
            .setRole(User.RoleType.ADMIN)
            .build();
    }
}
