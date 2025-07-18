package com.devdo.follow.api;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.follow.api.dto.response.FollowResDto;
import com.devdo.follow.application.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FollowController {

    private final FollowService followService;

    @Operation(summary = "팔로우 요청", description = "로그인한 사용자가 다른 사람에게 팔로우 요청을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping("/follow")
    public ApiResTemplate<String> follow(@RequestParam Long toMemberId, Principal principal) {
        followService.follow(toMemberId, principal);
        return ApiResTemplate.successWithNoContent(SuccessCode.FOLLOW_SUCCESS);
    }

    @Operation(summary = "언팔로우 요청", description = "로그인한 사용자가 다른 사람에게 언팔로우 요청을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @DeleteMapping("/unfollow")
    public ApiResTemplate<String> unfollow(@RequestParam Long toMemberId, Principal principal) {
        followService.unfollow(toMemberId, principal);
        return ApiResTemplate.successWithNoContent(SuccessCode.FOLLOW_DELETE_SUCCESS);
    }

    @Operation(summary = "팔로잉 조회", description = "로그인한 사용자가 자신 or 다른 사람의 팔로잉 리스트를 memberId로 판별해 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/{memberId}/following")
    public ApiResTemplate<FollowResDto> getFollowing(@PathVariable("memberId") Long memberId, Principal principal) {
        FollowResDto followResDto = followService.getFollowing(memberId, principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, followResDto);
    }

    @Operation(summary = "팔로워 조회", description = "로그인한 사용자가 자신 or 다른 사람의 팔로워 리스트를 memberId로 판별해 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/{memberId}/follower")
    public ApiResTemplate<FollowResDto> getFollower(@PathVariable("memberId") Long memberId, Principal principal) {
        FollowResDto followResDto = followService.getFollower(memberId, principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, followResDto);
    }
}
