package com.devdo.roadmap.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoadmapMainResponseDto(
        Long roadmapId,
        String title,
        String memberNickname,
        LocalDateTime createdAt
) {
}
