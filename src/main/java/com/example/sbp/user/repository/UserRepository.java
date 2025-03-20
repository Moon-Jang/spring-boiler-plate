package com.example.sbp.user.repository;

import com.example.sbp.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);
}
