package com.devdo.like.domain;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @Column(name = "like_created_at")
    private LocalDateTime likeCreatedAt;

    // 한 명이 여러 개의 좋아요를 누를 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 하나의 커뮤니티 게시글에 여러 개의 좋아요가 존재한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public Like(Member member, Community community) {
        this.member = member;
        this.community = community;
        this.likeCreatedAt = LocalDateTime.now();
    }
}
