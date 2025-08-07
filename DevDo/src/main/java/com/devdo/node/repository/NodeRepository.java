package com.devdo.node.repository;


import com.devdo.node.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {

    List<Node> findByRoadmap_Id(Long roadmapId);
    List<Node> findByParentNode_NodeId(Long parentId);
}

