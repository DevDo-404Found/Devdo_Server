package com.devdo.global.oauth2;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.error.SuccessCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.global.jwt.JwtTokenProvider;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthLoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    // refreshToken 저장
    public ResponseEntity<ApiResTemplate<String>> loginSuccess(Member member) {
        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        // DB에 리프레시 토큰 저장
        member.saveRefreshToken(refreshToken);
        memberRepository.save(member);

        // HttpOnly 쿠키로 리프레시 토큰 전달
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Long.parseLong(jwtTokenProvider.getRefreshTokenExpireTime()) / 1000)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResTemplate.successResponse(SuccessCode.LOGIN_SUCCESS, accessToken));
    }

    // refreshToken으로 새로운 accessToken 생성
    public ResponseEntity<ApiResTemplate<String>> reissueAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION
                        , ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage()));

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "리프레시 토큰이 만료되었습니다.");
        }

        String newAccessToken = jwtTokenProvider.generateToken(member);
        return ResponseEntity.ok(ApiResTemplate.successResponse(SuccessCode.REFRESH_TOKEN_SUCCESS, newAccessToken));
    }
}
