package com.devdo.comment.domain;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /*
    부모 댓글 - 하나의 부모 댓글에는 여러 개의 자식 댓글이 달릴 수 있다.
    NULL일 경우 자식 댓글 X
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    /*
    자식 댓글 리스트 - 여러 개의 자식 댓글이 하나의 부모 댓글에 달릴 수 있다.
     */
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    // 한 명의 사용자는 여러 개의 댓글을 작성할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 한 개의 게시글에는 여러 개의 댓글이 달릴 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public Comment(String content, Community community, Member member, Comment parentComment) {
        this.content = content;
        this.community = community;
        this.member = member;
        this.parentComment = parentComment;
        this.commentCreatedAt = LocalDateTime.now();
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
