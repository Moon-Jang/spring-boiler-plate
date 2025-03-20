package com.example.sbp.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class PostgreSQLTestContainerConfig {

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
        POSTGRESQL_CONTAINER.start();
    }

    @Bean
    public PostgreSQLContainer<?> postgresqlContainer() {
        return POSTGRESQL_CONTAINER;
    }

    // 정적 메서드를 통해 컨테이너 인스턴스를 반환할 수 있음
    public static PostgreSQLContainer<?> getContainer() {
        return POSTGRESQL_CONTAINER;
    }
} 