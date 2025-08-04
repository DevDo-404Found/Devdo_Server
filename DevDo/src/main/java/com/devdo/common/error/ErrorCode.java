package com.devdo.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400 BAD REQUEST
    ALREADY_FOLLOW_STATE(HttpStatus.BAD_REQUEST, "이미 팔로우 중입니다.", "BAD_REQUEST_400"),
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST, "자기 자신은 팔로우할 수 없습니다.", "BAD_REQUEST_400"),
    NOT_FOLLOW_STATE(HttpStatus.BAD_REQUEST, "팔로우 상태가 아닙니다.", "BAD_REQUEST_400"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검사에 실패하였습니다.", "BAD_REQUEST_400"),
    NOT_CHILD_COMMENT_HIERARCHY(HttpStatus.BAD_REQUEST, "대댓글의 대댓글은 허용하지 않습니다.", "BAD_REQUEST_400"),
    ALREADY_EXISTS_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.", "BAD_REQUEST_400"),
    ALREADY_EXISTS_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.", "BAD_REQUEST_400"),

    // 401 UNAUTHORIZED
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "JWT가 비어있거나 잘못된 값입니다.", "UNAUTHORIZED_401"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT가 만료되었습니다.", "UNAUTHORIZED_401"),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 형식입니다.", "UNAUTHORIZED_401"),
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "잘못된 JWT 토큰입니다.", "UNAUTHORIZED_401"),
    JWT_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "JWT 서명 검증에 실패했습니다.", "UNAUTHORIZED_401"),
    JWT_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT 검증에 실패했습니다.", "UNAUTHORIZED_401"),
    NO_AUTHORIZATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.", "UNAUTHORIZED_401"),

    // 403 FORBIDDEN
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "FORBIDDEN_403"),

    // 404 NOT FOUND
    COMMENT_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다. commentId = ", "NOT_FOUND_404"),
    MEMBER_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 사용자가 없습니다. memberId = ", "NOT_FOUND_404"),
    COMMUNITY_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다. communityId = ", "NOT_FOUND_404"),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생했습니다", "INTERNAL_SERVER_ERROR_500");

    private final HttpStatus httpStatus;
    private final String message;
    private final String code;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}