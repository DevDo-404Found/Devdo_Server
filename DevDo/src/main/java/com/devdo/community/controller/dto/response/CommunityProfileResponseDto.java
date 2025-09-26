package com.devdo.community.controller.dto.response;

import com.devdo.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommunityProfileResponseDto(
        Long memberId,
        String profilePicture,
        String nickname,
        String title,
        @JsonFormat(pattern = "yy/MM/dd HH:mm")
        LocalDateTime createdAt,
        Long viewCount,
        int commentCount,
        int followerCount,
        int followingCount,
        boolean isFollowing,
        Boolean isMyProfile,
        List<CommunityAllResponseDto> myCommunities
) {
    public static CommunityProfileResponseDto from(Member member, String title, LocalDateTime createdAt,
                                                   Long viewCount, int commentCount,
                                                   int followerCount, int followingCount, boolean isFollowing,
                                                   Boolean isMyProfile,
                                                   List<CommunityAllResponseDto> myCommunities) {
        return new CommunityProfileResponseDto(
                member.getMemberId(),
                member.getPictureUrl(),
                member.getNickname(),
                title,
                createdAt,
                viewCount,
                commentCount,
                followerCount,
                followingCount,
                isFollowing,
                isMyProfile,
                myCommunities
        );
    }
}
