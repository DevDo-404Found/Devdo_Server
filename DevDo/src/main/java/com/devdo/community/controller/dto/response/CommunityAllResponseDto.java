package com.devdo.community.controller.dto.response;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommunityAllResponseDto(
        Long id,
        String title,
        @JsonFormat(pattern = "yy/MM/dd HH:mm")
        LocalDateTime createdAt,
        Long viewCount,
        int commentCount // 댓글 개수
) {
    public static CommunityAllResponseDto from(Community community, int commentCount) {
        Member member = community.getMember();

        return new CommunityAllResponseDto(
                community.getId(),
                community.getTitle(),
                community.getCreatedAt(),
                community.getViewCount(),
                commentCount
        );
    }
}
