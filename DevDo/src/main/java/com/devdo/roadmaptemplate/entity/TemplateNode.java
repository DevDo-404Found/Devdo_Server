package com.devdo.roadmaptemplate.entity;

import com.devdo.node.entity.NodeColor;
import com.devdo.node.entity.NodeShape;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TemplateNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nodeName;

    @Enumerated(EnumType.STRING)
    private NodeShape nodeShape;

    @Enumerated(EnumType.STRING)
    private NodeColor nodeColor;

    private String link;
    private Long parentNodeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_roadmap_id")
    private TemplateRoadmap templateRoadmap;
}