package com.devdo.roadmap.service;

import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.roadmap.controller.dto.request.RoadmapRequestDto;
import com.devdo.roadmap.controller.dto.response.RoadmapResponseDto;
import com.devdo.roadmap.entity.Roadmap;
import com.devdo.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createRoadmap(RoadmapRequestDto requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Roadmap roadmap = Roadmap.builder()
                .title(requestDto.title())
                .member(member)
                .build();

        roadmapRepository.save(roadmap);
        return roadmap.getId();
    }

    @Transactional(readOnly = true)
    public List<RoadmapResponseDto> getMyRoadmaps(Long memberId) {
        return roadmapRepository.findAllByMember_MemberId(memberId).stream()
                .map(r -> RoadmapResponseDto.builder()
                        .roadmapId(r.getId())
                        .title(r.getTitle())
                        .build()
                ).collect(toList());
    }

    @Transactional
    public void updateRoadmap(Long roadmapId, String newTitle) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 로드맵이 존재하지 않습니다."));
        roadmap.updateTitle(newTitle);
    }

    @Transactional
    public void deleteRoadmap(Long roadmapId) {
        roadmapRepository.deleteById(roadmapId);
    }
}
