package com.devdo.community.entity;

import com.devdo.community.controller.dto.request.CommunityRequestDto;
import com.devdo.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "viewCount", nullable = false)
    private Long viewCount;

    @Column(name = "likeCount", nullable = false)
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(CommunityRequestDto commnuityRequestDto) {
        if (commnuityRequestDto.title() != null) this.title = commnuityRequestDto.title();
        if (commnuityRequestDto.content() != null) this.content = commnuityRequestDto.content();
    }
}
