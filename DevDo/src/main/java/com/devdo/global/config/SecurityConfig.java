package com.devdo.global.config;

import com.devdo.global.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // 하나 이상의 @Bean 메서드를 가지고 있어 spring 컨테이너가 해당 메서드들을 관리하고 빈으로 등록
@EnableWebSecurity  // spring security 활성화, 웹 보안 설정 제공
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean // 보안 필터 체인을 정의하는 Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP 인증 비활성화
                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 스프링 기본 폼 로그인 비활성화
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 세션 정책을 stateless로 설정 -> 서버가 세션을 생성하지 않고 토큰 기반 인증을 사용하도록 설정 = jwt 방식
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/api/v1/login/**").permitAll()
                        .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()   // 그 외 모든 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() { // 패스워드 암호화
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}