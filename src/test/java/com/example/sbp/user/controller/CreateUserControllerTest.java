package com.example.sbp.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.example.sbp.common.ControllerTestContext;
import com.example.sbp.user.service.CreateUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.example.sbp.common.ApiDocumentUtils.*;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class CreateUserControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("회원가입");

    @MockBean
    private CreateUserService service;

    @Test
    void success() {
        var body = new CreateUserController.Request(
            "test1234",
            "password123"
        );

        given()
            .body(body)
            .when()
            .post("/users/sign-up")
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
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}