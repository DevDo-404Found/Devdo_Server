package com.devdo.follow.api.dto.response;

import com.devdo.member.api.dto.response.MemberInfoResDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FollowResDto(
    int followerCount,
    int followingCount,
    List<MemberInfoResDto> members,
    Boolean isFollowing
) {
    // 리스트 조회시 응답
    public static FollowResDto from(int followerCount, int followingCount, List<MemberInfoResDto> members) {
        return new FollowResDto(
                followerCount,
                followingCount,
                members,
                null
        );
    }

    // post, delete 요청 시 t/f 응답
    public static FollowResDto from(int followerCount, int followingCount, Boolean isFollowing) {
        return new FollowResDto(
                followerCount,
                followingCount,
                null,
                isFollowing
        );
    }
}
