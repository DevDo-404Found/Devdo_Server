package com.devdo.like.api;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.like.application.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 생성", description = "로그인한 사용자가 커뮤니티 게시글에 좋아요를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping
    public ApiResTemplate<Long> saveLike(@RequestParam Long communityId, Principal principal) {
        likeService.saveLike(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.LIKE_SAVE_SUCCESS, communityId);
    }

    @Operation(summary = "좋아요 삭제", description = "로그인한 사용자가 커뮤니티 게시글에 생성한 좋아요를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @DeleteMapping
    public ApiResTemplate<Long> deleteLike(@RequestParam Long communityId, Principal principal) {
        likeService.deleteLike(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.LIKE_DELETE_SUCCESS, communityId);
    }
}
