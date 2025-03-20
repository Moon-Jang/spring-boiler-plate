package com.example.sbp.integration;

import com.example.sbp.common.IntegrationTest;
import com.example.sbp.common.component.JwtTokenManager;
import com.example.sbp.user.domain.User;
import com.example.sbp.user.domain.UserFixture;
import com.example.sbp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityIntegrationTest extends IntegrationTest {
    private static final String TEST_ENDPOINT = "/users/me";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenManager jwtTokenManager;

    private User normalUser;
    private String jwtToken;


    @BeforeEach
    void setUp() {
        this.normalUser = userRepository.save(UserFixture.normalUser());
        this.jwtToken = jwtTokenManager.issue(normalUser.id(), normalUser.name());
    }
    
    @Test
    @DisplayName("유효한 토큰으로 인증된 요청은 성공한다")
    void authenticateWithValidToken() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("토큰이 없는 요청은 인증에 실패한다")
    void failAuthenticationWithoutToken() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 요청하면 인증에 실패한다")
    void failAuthenticationWithInvalidToken() throws Exception {
        var invalidToken = "invalid.token.value";

        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
    
    @Test
    @DisplayName("Bearer 접두사 없는 토큰은 인증에 실패한다")
    void failAuthenticationWithoutBearerPrefix() throws Exception {
        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
    
    @Test
    @DisplayName("존재하지 않는 사용자 ID로 생성된 토큰은 인증에 실패한다")
    void failAuthenticationWithNonExistentUserId() throws Exception {
        var token = jwtTokenManager.issue(9999L, "notExist");

        mockMvc.perform(
                get(TEST_ENDPOINT)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            )
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("토큰이 유효하지 않습니다."));
    }
}