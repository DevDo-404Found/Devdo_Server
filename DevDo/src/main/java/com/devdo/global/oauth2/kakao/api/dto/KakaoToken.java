package com.devdo.global.oauth2.kakao.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class KakaoToken {

    @SerializedName("access_token")
    private String accessToken;

}