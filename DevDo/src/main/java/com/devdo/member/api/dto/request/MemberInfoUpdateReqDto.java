package com.devdo.member.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateReqDto(
        @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
        @Size(min = 2, max = 20)
        String nickname,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String pictureUrl
) {
}
