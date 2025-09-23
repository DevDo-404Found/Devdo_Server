package com.devdo.roadmap.repository;

import com.devdo.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findAllByMember_MemberId(Long memberId);

    List<Roadmap> findAllByMember_MemberIdOrderBySortOrderAsc(Long memberId);
}
