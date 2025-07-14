package com.devdo.scrap.controller.dto;

import com.devdo.community.entity.Community;
import com.devdo.scrap.entity.Scrap;

import java.time.LocalDateTime;

public record ScrapResponseDto(
        Long id,
        String title,
        String content,
        LocalDateTime communityCreatedAt // 커뮤니티글 작성일

) {
    public static ScrapResponseDto from(Scrap scrap) {
        Community community = scrap.getCommunity();

        return new ScrapResponseDto(
                community.getId(),
                community.getTitle(),
                community.getContent(),
                community.getCreatedAt() // 커뮤니티글 작성일
        );
    }
}
