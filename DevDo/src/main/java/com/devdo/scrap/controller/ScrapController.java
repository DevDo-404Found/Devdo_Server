package com.devdo.scrap.controller;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.scrap.controller.dto.ScrapResponseDto;
import com.devdo.scrap.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community/scrap")
@Tag(name = "커뮤니티글 스크랩 API", description = "CommunityPage 스크랩 관련 API들 입니다.")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    @Operation(method = "POST", summary = "스크랩 추가", description = "스크랩을 추가합니다.")
    public ApiResTemplate<String> addScrap(@RequestParam Long communityId,
                                              Principal principal) {
        scrapService.saveScrap(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.SCRAP_SAVE_SUCCESS, "communityId : " + communityId + ", 스크랩 추가 완료");
    }

    @DeleteMapping
    @Operation(method = "DELETE", summary = "스크랩 삭제", description = "스크랩을 삭제합니다.")
    public ApiResTemplate<String> deleteScrap(@RequestParam Long communityId,
                                                 Principal principal) {
        scrapService.deletesScrap(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.SCRAP_DELETE_SUCCESS, "communityId : " + communityId + ", 스크랩 삭제 완료");
    }

    @GetMapping
    @Operation(method = "GET", summary = "내가 스크랩한 커뮤니티글 조회", description = "스크랩을 조회합니다.")
    public ResponseEntity<List<ScrapResponseDto>> getScraps(Principal principal) {
        return ResponseEntity.ok(scrapService.getMyScraps(principal));
    }

    @GetMapping("/count")
    @Operation(method = "GET", summary = "커뮤니티글의 스크랩 수 조회", description = "해당 커뮤니티 글의 스크랩 개수를 조회합니다.")
    public ApiResTemplate<String> getScrapCount(@RequestParam Long communityId) {
        Long scrapCount = scrapService.getScrapCount(communityId);
        return ApiResTemplate.successResponse(SuccessCode.SCRAP_COUNT_SUCCESS, "스크랩 수 : " + scrapCount);
    }
}
