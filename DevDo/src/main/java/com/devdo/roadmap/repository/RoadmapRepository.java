package com.devdo.roadmap.repository;

import com.devdo.roadmap.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findAllByMember_MemberId(Long memberId);
}
