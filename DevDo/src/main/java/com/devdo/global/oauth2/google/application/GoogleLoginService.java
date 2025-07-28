package com.devdo.global.oauth2.google.application;

import com.devdo.global.nickname.RandomNicknameService;
import com.devdo.global.oauth2.google.api.dto.GoogleToken;
import com.devdo.global.oauth2.google.api.dto.GoogleUserInfo;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.SocialType;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    private final RandomNicknameService randomNicknameService;

    // 구글 액세스 토큰 가져오기
    public String getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = Map.of(
                "code", code,
                "scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
                "client_id", GOOGLE_CLIENT_ID,
                "client_secret", GOOGLE_CLIENT_SECRET,
                "redirect_uri", GOOGLE_REDIRECT_URI,
                "grant_type", "authorization_code"
        );

        // 구글 토큰 URL로 post 요청 보내서 액세스 토큰 가져오는 로직
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {  // 요청 성공
            String json = responseEntity.getBody();
            Gson gson = new Gson();

            // json 응답을 token 객체로 변환해서 액세스 토큰 반환
            return gson.fromJson(json, GoogleToken.class)
                    .getAccessToken();
        }
        // 요청 실패 예외
        throw new RuntimeException("구글 액세스 토큰을 가져오는데 실패하였습니다.");
    }

    // 구글 액세스 토큰으로 사용자 정보 갖고오기
    public GoogleUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);  // Bearer 뒤에 공백 필요함 -> 아니면 로그인 안 됨
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String json = responseEntity.getBody();
            Gson gson = new Gson();
            // json 응답 userinfo 객체로 변환해서 반환
            return gson.fromJson(json, GoogleUserInfo.class);
        }

        // 요청 실패 예외
        throw new RuntimeException("유저 정보를 가져오는데 실패했습니다.");
    }

    public Member processLogin(String code) {
        String accessToken = getGoogleAccessToken(code);

        GoogleUserInfo userInfo = getUserInfo(accessToken);

        if (!userInfo.getVerifiedEmail()) {
            throw new RuntimeException("이메일 인증이 되지 않은 유저입니다.");
        }

        return randomNicknameService.findOrCreateMember(
                userInfo.getEmail(),
                userInfo.getPictureUrl(),
                SocialType.GOOGLE
        );
    }
}