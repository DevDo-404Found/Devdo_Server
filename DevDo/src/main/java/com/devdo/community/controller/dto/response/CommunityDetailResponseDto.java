package com.devdo.community.controller.dto.response;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommunityDetailResponseDto(
        Long id,
        String pictureUrl,
        String title,
        String content,
        @JsonFormat(pattern = "yy/MM/dd HH:mm")
        LocalDateTime createdAt,
        Long viewCount,
        Long viewLike,
        String memberNickname,
        int commentCount, // 댓글 개수
        Boolean isLiked
) {
    public static CommunityDetailResponseDto from(Community community, int commentCount, boolean isLiked) {
        Member member = community.getMember();

        return new CommunityDetailResponseDto(
                community.getId(),
                member.getPictureUrl(),
                community.getTitle(),
                community.getContent(),
                community.getCreatedAt(),
                community.getViewCount(),
                community.getLikeCount(),
                member.getNickname(),
                commentCount,
                isLiked
        );
    }
}
