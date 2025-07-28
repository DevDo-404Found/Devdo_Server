package com.devdo.comment.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentSaveReqDto(
        @NotBlank(message = "내용은 필수로 입력해야 합니다.")
        @Size(min = 10, max = 100)
        String content,

        @Schema(description = "부모 댓글 id, null로 보내면 부모 댓글로 간주", nullable = true)
        Long parentCommentId
) {
}
