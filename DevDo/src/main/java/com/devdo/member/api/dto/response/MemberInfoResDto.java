package com.devdo.member.api.dto.response;

import com.devdo.member.domain.Member;
import com.devdo.member.domain.SocialType;
import lombok.Builder;

@Builder
public record MemberInfoResDto(
    Long memberId,
    String email,
    String nickname,
    String pictureUrl,
    SocialType socialType,
    int followingCount,
    int followerCount
) {
    public static MemberInfoResDto from(Member member) {
        return MemberInfoResDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .pictureUrl(member.getPictureUrl())
                .socialType(member.getSocialType())
                .followerCount(member.getFollowerCount())
                .followingCount(member.getFollowingCount())
                .build();
    }
}
