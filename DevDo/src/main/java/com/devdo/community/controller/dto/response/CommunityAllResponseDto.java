package com.devdo.community.controller.dto.response;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;

import java.time.LocalDateTime;

public record CommunityAllResponseDto(
        Long id,
        String title,
        LocalDateTime createdAt,
        Long viewCount

) {
    public static CommunityAllResponseDto from(Community community) {
        Member member = community.getMember();

        return new CommunityAllResponseDto(
                community.getId(),
                community.getTitle(),
                community.getCreatedAt(),
                community.getViewCount()
        );
    }
}
