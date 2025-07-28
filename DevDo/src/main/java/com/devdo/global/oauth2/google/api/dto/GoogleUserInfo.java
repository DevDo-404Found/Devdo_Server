package com.devdo.global.oauth2.google.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GoogleUserInfo {
    private String id;

    private String email;

    @SerializedName("verified_email")
    private Boolean verifiedEmail;

    private String name;

    @SerializedName("given_name")
    private String givenName;

    @SerializedName("family_name")
    private String familyName;

    @SerializedName("picture")
    private String pictureUrl;

    private String locale;
}
