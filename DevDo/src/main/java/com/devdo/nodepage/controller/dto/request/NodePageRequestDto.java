package com.devdo.nodepage.controller.dto.request;

import com.devdo.common.jackson.NewlineSanitizer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record NodePageRequestDto(
        @JsonDeserialize(using = NewlineSanitizer.class)
        String content,
        String emoji,
        String pictureUrl
) {
}
