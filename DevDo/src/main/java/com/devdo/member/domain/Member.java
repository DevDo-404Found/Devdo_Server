package com.devdo.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.devdo.follow.domain.Follow;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_nickname")
    private String nickname;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @OneToMany(mappedBy = "fromMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "toMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @Column(name = "follower_count")
    private int followerCount = 0;

    @Column(name = "following_count")
    private int followingCount = 0;

    private String refreshToken;

    @Builder
    public Member(String nickname, String email, String pictureUrl, SocialType socialType, String refreshToken) {
        this.nickname = nickname;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    // follow count update
    public void updateFollowingCount(int count) {
        this.followingCount = Math.max(0, this.followingCount + count);
    }

    public void updateFollowerCount(int count) {
        this.followerCount = Math.max(0, this.followerCount + count);
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
