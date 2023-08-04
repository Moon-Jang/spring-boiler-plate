package com.example.sbp.common;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface ApiDocumentUtils {
    static OperationRequestPreprocessor preprocessRequest() {
        return Preprocessors.preprocessRequest(prettyPrint());
    }

    static OperationResponsePreprocessor preprocessResponse() {
        return Preprocessors.preprocessResponse(prettyPrint());
    }

//    private static RequestHeadersSnippet requestHeaderWithAuth(String headerName, String description) {
//        return HeaderDocumentation.requestHeaders(
//            List.of(headerWithName(headerName).description(description))
//        );
//    }

    static List<FieldDescriptor> fieldsWithBasic(FieldDescriptor... fieldDescriptors) {
        return Stream.concat(
                Stream.of(
                    fieldWithPath("status").type(JsonFieldType.STRING).description("결과 상태"),
                    fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("응답 시간 (yyyy-MM-dd HH:mm:ss)"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메시지")
                ),
                Arrays.stream(fieldDescriptors)
            )
            .toList();
    }
}
