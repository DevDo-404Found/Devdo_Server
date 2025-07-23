package com.devdo.comment.domain;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_content")
    private String content;

    @Column(name = "comment_created_at")
    private LocalDateTime commentCreatedAt;

    // 한 명의 사용자는 여러 개의 댓글을 작성할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 한 개의 게시글에는 여러 개의 댓글이 달릴 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public Comment(String content, Community community, Member member) {
        this.content = content;
        this.community = community;
        this.member = member;
        this.commentCreatedAt = LocalDateTime.now();
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
