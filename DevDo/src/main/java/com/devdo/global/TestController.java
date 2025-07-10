package com.devdo.global;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ApiResTemplate<String> test() {
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, "test");
    }
}
