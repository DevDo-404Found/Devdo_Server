package com.devdo.roadmaptemplate.repository;


import com.devdo.roadmaptemplate.entity.TemplateNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateNodeRepository extends JpaRepository<TemplateNode, Long> {

    // 특정 템플릿 로드맵에 속한 모든 노드를 찾음
    List<TemplateNode> findAllByTemplateRoadmapId(Long templateRoadmapId);

    // 계층 구조 복사 (부모 노드 ID로 자식 노드를 찾음)
    List<TemplateNode> findAllByParentNodeId(Long parentNodeId);
}