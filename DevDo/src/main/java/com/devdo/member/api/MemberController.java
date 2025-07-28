package com.devdo.member.api;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.member.api.dto.request.MemberInfoUpdateReqDto;
import com.devdo.member.api.dto.response.MemberInfoResDto;
import com.devdo.member.application.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자가 자신의 프로필 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/profile")
    public ApiResTemplate<MemberInfoResDto> getMemberInfo(Principal principal) {
        MemberInfoResDto memberInfoResDto = memberService.getMemberInfo(principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, memberInfoResDto);
    }

    @Operation(summary = "내 정보 수정", description = "로그인한 사용자가 자신의 프로필 정보(nickname)를 수정합니다." +
            "\n pictureUrl은 null로 보내면 삭제 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PatchMapping("/profile")
    public ApiResTemplate<MemberInfoResDto> updateMemberInfo(@RequestBody @Valid MemberInfoUpdateReqDto memberInfoUpdateReqDto,
                                                             Principal principal) {
        MemberInfoResDto memberInfoResDto = memberService.updateMemberInfo(memberInfoUpdateReqDto, principal);
        return ApiResTemplate.successResponse(SuccessCode.MEMBER_INFO_UPDATE_SUCCESS, memberInfoResDto);
    }
}
