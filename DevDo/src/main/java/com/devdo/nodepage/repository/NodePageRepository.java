package com.devdo.nodepage.repository;

import com.devdo.nodepage.entity.NodePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodePageRepository extends JpaRepository<NodePage, Long> {
    Optional<NodePage> findByNode_NodeId(Long nodeId);
}
