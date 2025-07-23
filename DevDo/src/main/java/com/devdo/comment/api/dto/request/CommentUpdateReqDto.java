package com.devdo.comment.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateReqDto(
        @NotBlank(message = "내용은 필수로 입력해야 합니다.")
        @Size(min = 10, max = 100)
        String content
) {
}
