package com.devdo.community.entity;

import com.devdo.community.controller.dto.request.CommunityRequestDto;
import com.devdo.member.domain.Member;
import com.devdo.scrap.entity.Scrap;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count",  nullable = false)
    private Long viewCount;

    @Column(name = "like_count",  nullable = false)
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "scrap_count", nullable = false)
    private Long scrapCount;

    @OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    // 자동 생성
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
        if (this.likeCount == null) {
            this.likeCount = 0L;
        }
        if (this.scrapCount == null) {
            this.scrapCount = 0L;
        }
    }

    // 스크랩 개수 관련
    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        if (this.scrapCount > 0) this.scrapCount--;
    }

    // 조회수 관련
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(CommunityRequestDto commnuityRequestDto) {
        if (commnuityRequestDto.title() != null) this.title = commnuityRequestDto.title();
        if (commnuityRequestDto.content() != null) this.content = commnuityRequestDto.content();
    }
}
