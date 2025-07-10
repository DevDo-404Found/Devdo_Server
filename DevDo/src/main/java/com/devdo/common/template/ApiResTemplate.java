package com.devdo.common.template;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.error.SuccessCode;
import lombok.*;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ApiResTemplate<T> {

    private final int code;       // 응답 코드 (200, 404 등)
    private final String message; // 응답 메시지
    private T data;               // 응답 데이터

    // 데이터 없는 성공 응답
    public static ApiResTemplate successWithNoContent(SuccessCode successCode) {
        return new ApiResTemplate<>(successCode.getHttpStatusCode(), successCode.getMessage());
    }

    // 데이터 포함한 성공 응답
    public static <T> ApiResTemplate<T> successResponse(SuccessCode successCode, T data) {
        return new ApiResTemplate<>(successCode.getHttpStatusCode(), successCode.getMessage(), data);
    }

    // 에러 응답 (커스텀 메시지 포함)
    public static ApiResTemplate errorResponse(ErrorCode errorCode, String customMessage) {
        return new ApiResTemplate<>(errorCode.getHttpStatusCode(), customMessage);
    }

}
