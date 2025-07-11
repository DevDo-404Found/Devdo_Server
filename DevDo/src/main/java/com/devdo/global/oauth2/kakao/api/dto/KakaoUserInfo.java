package com.devdo.global.oauth2.kakao.api.dto;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakao_account;
    private Properties properties;

    @Data
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Data
        public static class Profile {
            private String profile_image_url;
        }
    }

    @Data
    public static class Properties {
        private String nickname;
    }
}
