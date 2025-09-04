package com.devdo.roadmap.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RoadmapMainResponseDto(
        Long roadmapId,
        String title,
        String memberNickname,
        @JsonFormat(pattern = "yy/MM/dd HH:mm")
        LocalDateTime createdAt
) {
}
