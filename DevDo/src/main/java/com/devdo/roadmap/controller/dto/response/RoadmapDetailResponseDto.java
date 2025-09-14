package com.devdo.roadmap.controller.dto.response;

import com.devdo.roadmap.entity.Roadmap;

import java.util.List;

public record RoadmapDetailResponseDto(
        Long roadmapId,
        String roadmapTitle,
        List<NodeInfoDto> nodes
) {
    public static RoadmapDetailResponseDto from(Roadmap roadmap) {
        List<NodeInfoDto> nodeDtos = roadmap.getNodes().stream()
                .map(NodeInfoDto::from)
                .toList();

        return new RoadmapDetailResponseDto(
                roadmap.getId(),
                roadmap.getTitle(),
                nodeDtos
        );
    }
}