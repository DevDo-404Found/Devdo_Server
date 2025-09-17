package com.devdo.member.api.dto.response;

import com.devdo.member.domain.Member;
import com.devdo.member.domain.SocialType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberInfoResDto(
    Long memberId,
    String email,
    String nickname,
    String pictureUrl,
    SocialType socialType,
    int followingCount,
    int followerCount,
    Boolean isFollowing,
    Boolean isMyProfile
) {
    public static MemberInfoResDto from(Member member, Boolean isFollowing, Boolean isMyProfile) {
        return MemberInfoResDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .pictureUrl(member.getPictureUrl())
                .socialType(member.getSocialType())
                .followerCount(member.getFollowerCount())
                .followingCount(member.getFollowingCount())
                .isFollowing(isFollowing)
                .isMyProfile(isMyProfile)
                .build();
    }

    // 내 프로필 조회
    public static MemberInfoResDto from(Member member) {
        return MemberInfoResDto.from(member, null, true);
    }
}
