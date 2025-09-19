package com.devdo.nodepage.repository;

import com.devdo.nodepage.entity.NodePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NodePageRepository extends JpaRepository<NodePage, Long> {
    Optional<NodePage> findByNode_NodeId(Long nodeId);

    // Node와 연결되지 않고 사진이 있는 NodePage를 찾는 새로운 쿼리 메서드
    List<NodePage> findByPictureUrlIsNotNullAndNodeIsNullAndCreatedAtBefore(LocalDateTime timestamp);
}
