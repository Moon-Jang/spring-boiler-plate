package com.example.sbp.common;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Import(ControllerTestContext.ControllerTestConfig.class)
@SpringBootTest
@ActiveProfiles("local")
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTestContext {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .build();
    }

    protected MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given().mockMvc(mockMvc)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8));
    }

    protected String identifier() {
        return getClass().getSimpleName();
    }

    protected String identifier(String affix) {
        return "%s-%s".formatted(identifier(), affix);
    }

    protected <T extends Enum<?>> ParameterDescriptor enumDescription(ParameterDescriptor descriptor,
                                                                      Class<T> enumClass) {
        return descriptor.description(
            "%s : %s".formatted(
                descriptor.getDescription(),
                Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "))
            )
        );
    }

    protected enum Tags {
        PRODUCT("상품");

        private final String tagName;

        Tags(String tagName) {
            this.tagName = tagName;
        }

        public String tagName() {
            return tagName;
        }

        public String descriptionWith(String affix) {
            return "%s : %s".formatted(tagName, affix);
        }
    }

    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        public BeanFactoryPostProcessor beanFactoryPostProcessor() {
            return (beanFactory) -> {
                var serviceList = beanFactory.getBeanNamesForAnnotation(Service.class);
                for (String service : serviceList) {
                    var type = beanFactory.getType(service);
                    ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(service);
                    beanFactory.registerSingleton(service, mock(type));
                }
            };
        }
    }
}
