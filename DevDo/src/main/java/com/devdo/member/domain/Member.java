package com.devdo.member.domain;

import com.devdo.scrap.entity.Scrap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @Builder
    public Member(String nickname, String email, String pictureUrl, SocialType socialType, String refreshToken) {
        this.nickname = nickname;
        this.email = email;
        this.pictureUrl = pictureUrl;
        this.socialType = socialType;
        this.refreshToken = refreshToken;
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
