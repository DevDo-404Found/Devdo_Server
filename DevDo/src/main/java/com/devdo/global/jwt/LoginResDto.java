package com.devdo.global.jwt;

public record LoginResDto(
        String accessToken,
        Long memberId
) {
}
