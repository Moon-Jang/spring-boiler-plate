package com.example.sbp.user.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.example.sbp.common.ControllerTestContext;
import com.example.sbp.user.domain.User;
import com.example.sbp.user.service.GetUserService;
import com.example.sbp.user.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.example.sbp.common.ApiDocumentUtils.*;
import static com.example.sbp.common.TestUserDetailsConfig.USER_DETAILS_BEAN_NAME;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public class GetUserControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("조회");

    @MockBean
    private GetUserService service;

    @WithUserDetails(userDetailsServiceBeanName=USER_DETAILS_BEAN_NAME)
    @Test
    void success() {
        BDDMockito.given(service.get(anyLong()))
            .willReturn(new UserVo(
                1L,
                "작성자명",
                User.RoleType.USER
            ));

        given()
            .header(authorizationHeader())
            .when()
            .get("/users/me")
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
                    requestHeaderWithAuthorization(),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                            fieldWithPath("data.id").type(NUMBER).description("사용자 ID"),
                            fieldWithPath("data.name").type(STRING).description("사용자 이름"),
                            enumDescription(
                                fieldWithPath("data.role").type(STRING).description("사용자 역할"),
                                User.RoleType.class
                            )
                        )
                    )
                )
            );
    }
}
