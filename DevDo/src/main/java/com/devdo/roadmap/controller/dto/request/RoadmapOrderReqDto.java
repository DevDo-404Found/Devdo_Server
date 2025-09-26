package com.devdo.roadmap.controller.dto.request;

import java.util.List;

public record RoadmapOrderReqDto(
        List<Long> roadmapId
) {
}
