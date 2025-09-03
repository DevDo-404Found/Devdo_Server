package com.devdo.community.controller.dto.response;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import com.devdo.member.util.MemberInfoHelper;

import java.time.LocalDateTime;

public record CommunityDetailResponseDto(
        Long id,
        String nickname,
        String pictureUrl,
        String title,
        String content,
        LocalDateTime createdAt,
        Long viewCount,
        Long viewLike,

        int commentCount // 댓글 개수
) {
    public static CommunityDetailResponseDto from(Community community, int commentCount) {
        Member member = community.getMember();

        return new CommunityDetailResponseDto(
                community.getId(),
                MemberInfoHelper.getMemberNickname(member),
                MemberInfoHelper.getMemberPictureUrl(member),
                community.getTitle(),
                community.getContent(),
                community.getCreatedAt(),
                community.getViewCount(),
                community.getLikeCount(),
                commentCount
        );
    }
}
