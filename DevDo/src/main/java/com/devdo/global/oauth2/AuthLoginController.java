package com.devdo.global.oauth2;

import com.devdo.global.oauth2.google.api.dto.GoogleToken;
import com.devdo.global.oauth2.google.application.GoogleLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class AuthLoginController {

    private final GoogleLoginService googleLoginService;

    @Operation(summary = "구글 로그인", description = "구글 로그인 콜백 api입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/google")
    public GoogleToken googleCallback(@RequestParam(name = "code") String code){
        String googleAccessToken = googleLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    public GoogleToken loginOrSignup(String googleAccessToken){
        return googleLoginService.loginOrSignUp(googleAccessToken);
    }

}
