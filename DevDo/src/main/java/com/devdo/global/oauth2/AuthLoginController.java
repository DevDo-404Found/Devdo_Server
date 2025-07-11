package com.devdo.global.oauth2;

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

}
