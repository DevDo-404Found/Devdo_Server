package com.devdo.nodepage.controller.dto.response;

public record NodePageResponseDto(
        Long nodePageId,
        String title,
        String content,
        String emoji,
        String pictureUrl
) {
}
