package com.devdo.community.controller.dto.response;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;

import java.time.LocalDateTime;

public record CommunityDetailResponseDto(
        Long id,
        String pictureUrl,
        String title,
        String content,
        LocalDateTime createdAt,
        Long viewCount,
        Long viewLike

        // Todo: 댓글 개수 추가하기
) {
    public static CommunityDetailResponseDto from(Community community) {
        Member member = community.getMember();

        return new CommunityDetailResponseDto(
                community.getId(),
                member.getPictureUrl(),
                community.getTitle(),
                community.getContent(),
                community.getCreatedAt(),
                community.getViewCount(),
                community.getLikeCount()
        );
    }
}
