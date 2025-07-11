package com.devdo.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    // 모든 요청이 들어올 때 필터가 가로채 인증 로직 실행
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
            IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request); // 요청에서 토큰 resolve(추출)

        if (token != null && jwtTokenProvider.validateToken(token)) {   // 토큰이 유효한지 확인
            Authentication authentication = jwtTokenProvider.getAuthentication(token);  // 유효하다면 토큰으로부터 인증 정보 가져옴
            SecurityContextHolder.getContext().setAuthentication(authentication);   // SecurityContext에 인증 정보 저장 -> 이후 요청 처리에서 인증된 사용자 정보에 접근할 수 있음
        }
        chain.doFilter(request, response); // 필터 체인의 다음 필터로 요청 넘기기
    }

    // 요청에서 토큰 추출 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");    // 헤더에서 Authorization 값 꺼내기
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);    // "Bearer " 부분을 잘라내고 토큰만 리턴
        }
        return null;
    }
}
