package com.devdo.roadmap.controller;

import com.devdo.roadmap.controller.dto.request.RoadmapRequestDto;
import com.devdo.roadmap.controller.dto.response.RoadmapMainResponseDto;
import com.devdo.roadmap.controller.dto.response.RoadmapResponseDto;
import com.devdo.roadmap.service.RoadmapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
                              Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return roadmapService.createRoadmap(roadmapRequestDto, memberId);
    }

    @GetMapping("/my")
    @Operation(method = "GET", summary = "내 로드맵 조회", description = "로드맵을 조회합니다.")
    public List<RoadmapResponseDto> getMyRoadmaps(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return roadmapService.getMyRoadmaps(memberId);
    }

    @GetMapping("/main")
    @Operation(method = "GET", summary = "메인 로드맵 목록 조회", description = "사용자의 메인 로드맵 목록을 조회합니다.")
    public List<RoadmapMainResponseDto> getMainRoadmaps(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        return roadmapService.getMainRoadmaps(memberId);
    }

    @PutMapping("/title/{roadmapId}")
    @Operation(method = "PUT", summary = "로드맵 수정", description = "로드맵을 수정합니다.")
    public void updateRoadmap(@PathVariable Long roadmapId,
                              @RequestParam String newTitle,
                              Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        roadmapService.updateRoadmap(roadmapId, newTitle, memberId);
    }

    @DeleteMapping("/{roadmapId}")
    @Operation(method = "DELETE", summary = "로드맵 삭제", description = "로드맵을 삭제합니다.")
    public void deleteRoadmap(@PathVariable Long roadmapId,
                              Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        roadmapService.deleteRoadmap(roadmapId, memberId);
    }
}
