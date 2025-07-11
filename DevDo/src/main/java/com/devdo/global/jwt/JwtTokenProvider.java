package com.devdo.global.jwt;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j  // 로그 작성
@Component  // spring bean에 등록
@Getter
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY= "auth";
    private final MemberRepository memberRepository;

    @Value("${token.expire.time}")
    private String tokenExpireTime; // 토큰 만료 시간

    @Value("${refresh-token.expire.time}")
    private String refreshTokenExpireTime;

    @Value("${jwt.secret}")
    private String secret;  // 비밀키

    private SecretKey key;  // 객체 key

    public JwtTokenProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct  // Bean이 초기화 된 후에 실행
    public void init() {    // 필터 객체를 초기화하고 서비스에 추가하기 위한 메소드 init
        byte[] keyBytes = Decoders.BASE64.decode(secret);   // 시크릿 키 디코딩 후
        this.key = Keys.hmacShaKeyFor(keyBytes); // 키 암호화
    }

    // 토큰 생성
    public String generateToken(Member member) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + Long.parseLong(tokenExpireTime));

        return Jwts.builder()
                .subject(member.getMemberId().toString())   // 토큰 주체를 id로 설정
                .claim(AUTHORITIES_KEY, member.getSocialType().toString())
                .issuedAt(now)  // 발행 시간
                .expiration(expireDate) // 만료 시간
                .signWith(key, Jwts.SIG.HS256)  // ㅏ토큰 암호화
                .compact(); // 압축, 서명 후 토큰 생성
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);  // 토큰 파싱, 검증
            return true;    // 검증 완료 -> 유효한 토큰
            // 검증 실패 시 반환하는 예외에 따라 다르게 실행
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 유효하지 않습니다.");
        } catch (SignatureException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 서명 검증에 실패했습니다.");
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 null 이거나 비어있거나 공백만 있습니다.");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 검증에 실패했습니다.");
        }
    }

    // 인증 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        // claim 정보를 통해 memberId 추출
        Long memberId = Long.parseLong(claims.getSubject());

        // memberId로 member 엔티티 조회
        Member member = memberRepository.findById(memberId).orElseThrow();

        // 권한이나 역할의 이름을 반환하는 메소드로 member.getRole()을 문자열로 만들어 배열로 리턴
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(member.getSocialType().toString())
        );

        // memberId, 공백, authorities 반환
        return new UsernamePasswordAuthenticationToken(member.getMemberId(), "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            return parser.parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // refreshToken 생성
    public String generateRefreshToken(Member member) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + Long.parseLong(refreshTokenExpireTime));

        return Jwts.builder()
                .subject(member.getMemberId().toString())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

}
