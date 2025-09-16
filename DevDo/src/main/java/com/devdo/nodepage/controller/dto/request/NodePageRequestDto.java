package com.devdo.nodepage.controller.dto.request;

import io.micrometer.common.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record NodePageRequestDto(
        String content,
        String emoji,
        MultipartFile pictureFile
) {
}
