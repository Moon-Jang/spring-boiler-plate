package com.example.sbp.common;

import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;


@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
@Transactional
@Import({PostgreSQLTestContainerConfig.class, TestUserDetailsConfig.class})
public class IntegrationTest {
    static PostgreSQLContainer<?> postgreContainer = PostgreSQLTestContainerConfig.getContainer();

    @Autowired
    protected MockMvc mockMvc;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreContainer::getUsername);
        registry.add("spring.datasource.password", postgreContainer::getPassword);
    }
}
