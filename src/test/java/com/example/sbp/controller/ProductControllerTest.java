package com.example.sbp.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.example.sbp.common.ControllerTestContext;
import com.example.sbp.service.ProductService;
import com.example.sbp.vo.ProductVo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.example.sbp.common.ApiDocumentUtils.fieldsWithBasic;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class ProductControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.PRODUCT.tagName();
    private static final String DESCRIPTION = Tags.PRODUCT.descriptionWith("상품 상세 조회");

    @Autowired
    private ProductService service;

    @Test
    void success() {
        Mockito.when(service.findById(eq(1L)))
            .thenReturn(mockResult());

        given()
            .when()
            .get(
                "/v1/products/{productId}",
                1L
            )
            .then()
            .log().all()
            .apply(
                document(
                    identifier("findById"),
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .description(DESCRIPTION),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("결과 데이터"),
                            fieldWithPath("data.id").type(NUMBER).description("상품 ID"),
                            fieldWithPath("data.name").type(STRING).description("상품 이름")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }

    private ProductVo mockResult() {
        return new ProductVo(
            1L,
            "testName"
        );
    }
}