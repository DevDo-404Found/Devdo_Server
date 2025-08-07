package com.devdo.roadmap.controller;

import com.devdo.roadmap.controller.dto.request.RoadmapRequestDto;
import com.devdo.roadmap.controller.dto.response.RoadmapResponseDto;
import com.devdo.roadmap.service.RoadmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roadmap")
@RequiredArgsConstructor
@Tag(name = "로드맵 API", description = "Roadmap 관련 API들 입니다.")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @PostMapping
    @Operation(method = "POST", summary = "로드맵 추가", description = "로드맵 추가합니다.")
    public Long createRoadmap(@RequestBody RoadmapRequestDto roadmapRequestDto,
                              @RequestParam Long memberId) {
        return roadmapService.createRoadmap(roadmapRequestDto, memberId);
    }

    @GetMapping("/my")
    @Operation(method = "GET", summary = "로드맵 조회", description = "로드맵을 조회합니다.")
    public List<RoadmapResponseDto> getMyRoadmaps(@RequestParam Long memberId) {
        return roadmapService.getMyRoadmaps(memberId);
    }

    @PutMapping("/title/{roadmapId}")
    @Operation(method = "PUT", summary = "로드맵 수정", description = "로드맵을 수정합니다.")
    public void updateRoadmap(@PathVariable Long roadmapId,
                              @RequestParam String newTitle) {
        roadmapService.updateRoadmap(roadmapId, newTitle);
    }

    @DeleteMapping("/{roadmapId}")
    @Operation(method = "DELETE", summary = "로드맵 삭제", description = "로드맵을 삭제합니다.")
    public void deleteRoadmap(@PathVariable Long roadmapId) {
        roadmapService.deleteRoadmap(roadmapId);
    }
}
