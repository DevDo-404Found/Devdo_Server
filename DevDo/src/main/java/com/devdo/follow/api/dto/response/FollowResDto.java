package com.devdo.follow.api.dto.response;

import com.devdo.member.api.dto.response.MemberInfoResDto;

import java.util.List;

public record FollowResDto(
    int followerCount,
    int followingCount,
    List<MemberInfoResDto> members
) {
    public static FollowResDto from(int followerCount, int followingCount, List<MemberInfoResDto> members) {
        return new FollowResDto(
                followerCount,
                followingCount,
                members
        );
    }
}
