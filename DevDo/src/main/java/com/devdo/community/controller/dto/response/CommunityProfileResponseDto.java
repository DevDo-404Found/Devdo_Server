package com.devdo.community.controller.dto.response;

import com.devdo.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommunityProfileResponseDto(
        String profilePicture,
        String nickname,
        String title,
        @JsonFormat(pattern = "yy/MM/dd HH:mm")
        LocalDateTime createdAt,
        Long viewCount,
        int commentCount,
        int followerCount,
        int followingCount
) {
    public static CommunityProfileResponseDto from(Member member, String title, LocalDateTime createdAt,
                                                   Long viewCount, int commentCount) {
        return new CommunityProfileResponseDto(
                member.getPictureUrl(),
                member.getNickname(),
                title,
                createdAt,
                viewCount,
                commentCount,
                member.getFollowerCount(),
                member.getFollowingCount()
        );
    }
}
