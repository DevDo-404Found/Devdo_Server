package com.devdo.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {

    // 200 OK
    GET_SUCCESS(HttpStatus.OK, "성공적으로 조회했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    FOLLOW_SUCCESS(HttpStatus.OK,"팔로우 요청에 성공하였습니다."),
    FOLLOW_DELETE_SUCCESS(HttpStatus.OK,"언팔로우 요청에 성공하였습니다."),
    MEMBER_UPDATE_SUCCESS(HttpStatus.OK, "사용자가 성공적으로 수정되었습니다."),
    COMMUNITY_UPDATE_SUCCESS(HttpStatus.OK, "글이 성공적으로 수정되었습니다."),
    MEMBER_DELETE_SUCCESS(HttpStatus.OK, "사용자가 성공적으로 삭제되었습니다."),
    COMMUNITY_DELETE_SUCCESS(HttpStatus.OK, "글이 성공적으로 삭제되었습니다."),
    SCRAP_DELETE_SUCCESS(HttpStatus.OK,"스크랩이 성공적으로 삭제되었습니다."),
    SCRAP_COUNT_SUCCESS(HttpStatus.OK, "스크랩 개수 조회에 성공했습니다."),

    // 201 CREATED
    REFRESH_TOKEN_SUCCESS(HttpStatus.CREATED, "리프레시 토큰으로 액세스 토큰 재발급에 성공하였습니다."),
    COMMUNITY_SAVE_SUCCESS(HttpStatus.CREATED, "글이 성공적으로 등록되었습니다."),
    SCRAP_SAVE_SUCCESS(HttpStatus.CREATED,"스크랩이 성공적으로 등록되었습니다."),
    MEMBER_JOIN_SUCCESS(HttpStatus.CREATED, "회원가입에 성공하였습니다."),
    MEMBER_LOGIN_SUCCESS(HttpStatus.CREATED, "로그인에 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
