package com.devdo.node.entity;

import com.devdo.nodepage.entity.NodePage;
import com.devdo.roadmap.entity.Roadmap;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long nodeId;

    @Column(nullable = false)
    private String nodeName;

    private String pictureUrl;

    private String link;

    @Enumerated(EnumType.STRING)
    private NodeShape nodeShape;

    @Enumerated(EnumType.STRING)
    private NodeColor nodeColor;

    // 연관 로드맵
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;

    // 부모 노드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_node_id")
    private Node parentNode;

    // 자식 노드
    @OneToMany(mappedBy = "parentNode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> children = new ArrayList<>();

    @OneToOne(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private NodePage nodePage;

    public void addChild(Node child) {
        children.add(child);
        child.parentNode = this;
    }

    public void updateNode(Roadmap roadmap, String nodeName, NodeShape nodeShape, NodeColor nodeColor, String pictureUrl, String link, Node parentNode) {
        this.roadmap = roadmap;
        this.nodeName = nodeName;
        this.nodeColor = nodeColor;
        this.nodeShape = nodeShape;
        this.pictureUrl = pictureUrl;
        this.link = link;
        this.parentNode = parentNode;
    }
}
