package com.devdo.global.oauth2;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.global.oauth2.google.application.GoogleLoginService;
import com.devdo.global.oauth2.kakao.application.KakaoLoginService;
import com.devdo.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class AuthLoginController {

    private final GoogleLoginService googleLoginService;
    private final KakaoLoginService kakaoLoginService;
    private final AuthLoginService authLoginService;

    @Operation(summary = "구글 로그인", description = "구글 로그인 콜백 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/google")
    public ResponseEntity<ApiResTemplate<String>> googleCallback(@RequestParam String code) {
        Member member = googleLoginService.processLogin(code);
        return authLoginService.loginSuccess(member);
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 콜백 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/kakao")
    public ResponseEntity<ApiResTemplate<String>> kakaoCallback(@RequestParam String code) {
        Member member = kakaoLoginService.processLogin(code);
        return authLoginService.loginSuccess(member);
    }

    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급", description = "쿠키의 리프레시 토큰으로 액세스 토큰을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰 없음 또는 만료")
    })
    @PostMapping("/refreshtoken")
    public ResponseEntity<ApiResTemplate<String>> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "리프레시 토큰이 없습니다.");
        }
        return authLoginService.reissueAccessToken(refreshToken);
    }
}
