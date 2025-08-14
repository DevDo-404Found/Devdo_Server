package com.devdo.ai.controller.dto;

public record AiRequestDto(
        Long nodeId,
        String queryType // lecture, article, curriculum
) {
}