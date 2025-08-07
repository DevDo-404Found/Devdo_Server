package com.devdo.roadmap.controller.dto.response;

import com.devdo.roadmap.entity.Roadmap;
import lombok.Builder;

@Builder
public record RoadmapResponseDto(
        Long roadmapId,
        String title
) {
    public static RoadmapResponseDto from(Roadmap roadmap) {
        return new RoadmapResponseDto(roadmap.getId(), roadmap.getTitle());
    }
}
