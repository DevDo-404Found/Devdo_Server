package com.devdo.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class NewlineSanitizer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) return null;
        // 줄바꿈이 들어오면 JSON escape 형태로 변환
        return value.replace("\r\n", "\n").replace("\r", "\n");
    }
}
