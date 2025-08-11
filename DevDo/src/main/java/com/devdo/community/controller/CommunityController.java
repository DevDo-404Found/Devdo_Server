package com.devdo.community.controller;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.community.controller.dto.request.CommunityRequestDto;
import com.devdo.community.controller.dto.response.CommunityAllResponseDto;
import com.devdo.community.controller.dto.response.CommunityDetailResponseDto;
import com.devdo.community.entity.Community;
import com.devdo.community.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/community")
@Tag(name = "커뮤니티 API", description = "CommunityPage 관련 API들 입니다.")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    @Operation(method = "POST", summary = "커뮤니티글 생성", description = "커뮤니티글을 생성합니다.")
    public ApiResTemplate<?> createWish(Principal principal, @Valid @RequestBody CommunityRequestDto commnuityRequestDto) {
        Long communityId = communityService.createCommunity(commnuityRequestDto, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMUNITY_SAVE_SUCCESS, communityId);
    }

    @PutMapping
    @Operation(method = "PUT", summary = "커뮤니티글 수정", description = "커뮤니티글을 수정합니다.")
    public ApiResTemplate<?> updateCommunity(
            Principal principal,
            @Valid @RequestParam Long communityId,
            @RequestBody CommunityRequestDto commnuityRequestDto
    ) {
        communityService.updateCommunity(communityId, commnuityRequestDto, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMUNITY_UPDATE_SUCCESS, communityId);
    }

    @DeleteMapping
    @Operation(method = "DELETE", summary = "커뮤니티글 삭제", description = "커뮤니티글을 삭제합니다.")
    public ApiResTemplate<?> deleteCommunity(Principal principal, @RequestParam Long communityId) {
        communityService.deleteCommunity(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMUNITY_DELETE_SUCCESS, communityId);
    }

    @GetMapping("/detail")
    @Operation(summary = "커뮤니티글 한 개 조회", description = "커뮤니티글 한 개를 조회합니다.")
    public ApiResTemplate<CommunityDetailResponseDto> getCommunity(@RequestParam Long communityId, Principal principal) {
        Community community = communityService.getCommunityWithRedisViewCount(communityId, principal);
        CommunityDetailResponseDto communityDetailResponseDto = CommunityDetailResponseDto.from(community);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, communityDetailResponseDto);
    }

    @GetMapping
    @Operation(method = "GET", summary = "커뮤니티글 전체 조회", description = "전체 커뮤니티글을 조회합니다.")
    public ApiResTemplate<List<CommunityAllResponseDto>> getAllCommunities() {
        List<CommunityAllResponseDto> communities = communityService.getAllCommunities();
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, communities);
    }

    @GetMapping("/my")
    @Operation(method = "GET", summary = "본인이 작성한 커뮤니티글 조회", description = "본인이 작성한 커뮤니티글만 조회합니다.")
    public ApiResTemplate<List<CommunityAllResponseDto>> getMyCommunities(Principal principal) {
        List<CommunityAllResponseDto> communities = communityService.getMyCommunities(principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, communities);
    }

    @GetMapping("/search")
    @Operation(method = "GET", summary = "커뮤니티글 제목 검색", description = "커뮤니티글 제목으로 검색합니다.")
    public ApiResTemplate<List<CommunityAllResponseDto>> searchCommunitiesByTitle(@RequestParam String keyword) {
        List<CommunityAllResponseDto> communities = communityService.searchCommunitiesByTitle(keyword);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, communities);
    }
}