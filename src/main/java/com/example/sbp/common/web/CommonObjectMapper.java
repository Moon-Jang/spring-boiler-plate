package com.example.sbp.common.web;

import com.example.sbp.common.support.Status;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS;

public class CommonObjectMapper extends ObjectMapper {
    public CommonObjectMapper() {
        setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        setVisibility(
            PropertyAccessor.FIELD,
            JsonAutoDetect.Visibility.ANY
        );
        registerModules(
            new JavaTimeModule(),
            new Jdk8Module(),
            new CustomModule()
        );
        disable(
            FAIL_ON_UNKNOWN_PROPERTIES
        );
        disable(
            WRITE_DATES_AS_TIMESTAMPS,
            WRITE_DURATIONS_AS_TIMESTAMPS
        );
    }

    private static class CustomModule extends SimpleModule {
        public CustomModule() {
            addSerializer(
                ZonedDateTime.class,
                new ZonedDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
                )
            );
            addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                )
            );
            addDeserializer(
                Status.class,
                new StdDeserializer<>(Status.class) {
                    @Override
                    public Status deserialize(JsonParser parser,
                                              DeserializationContext context) throws IOException {
                        var enumText = parser.getCodec()
                            .readValue(
                                parser,
                                String.class
                            );
                        return Status.from(enumText);
                    }
                }
            );
        }
    }
}
