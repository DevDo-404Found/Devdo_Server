package com.devdo.community.repository;

import com.devdo.community.entity.Community;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @EntityGraph(attributePaths = "member")
    Optional<Community> findWithMemberById(Long id);
    List<Community> findAllByMember_MemberId(Long memberId);
    List<Community> findByTitleContainingIgnoreCase(String keyword); // Containing: LIKE %keyword%
}
