package com.devdo.roadmaptemplate.repository;

import com.devdo.roadmaptemplate.entity.TemplateRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRoadmapRepository extends JpaRepository<TemplateRoadmap, Long> {

    // type으로 TemplateRoadmap 엔티티를 찾음
    Optional<TemplateRoadmap> findByType(String type);
}