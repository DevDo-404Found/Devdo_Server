package com.devdo.nodepage.entity;

import com.devdo.node.entity.Node;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NodePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_page_id")
    private Long nodePageId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String pictureUrl;

    // 페이지 상단의 이모지
    private String emoji;

    public String getTitle() {
        return node.getNodeName(); // node의 nodeName을 반환
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void updatePictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
