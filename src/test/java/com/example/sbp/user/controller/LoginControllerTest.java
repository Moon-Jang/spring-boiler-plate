package com.example.sbp.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.example.sbp.common.ControllerTestContext;
import com.example.sbp.user.service.LoginService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.example.sbp.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class LoginControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("로그인");

    @MockBean
    private LoginService service;

    @Test
    void success() {
        var body = new LoginController.Request(
            "test1234",
            "password123"
        );

        BDDMockito.given(service.login(any()))
            .willReturn("jwtToken");

        given()
            .body(body)
            .when()
            .post("/users/login")
            .then()
            .log().all()
            .apply(
                document(
                    identifier(),
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .description(DESCRIPTION),
                    preprocessRequest(),
                    preprocessResponse(),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                        fieldWithPath("password").type(STRING).description("비밀번호")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("JWT token")
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}