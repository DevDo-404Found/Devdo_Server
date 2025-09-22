package com.devdo.roadmap.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.node.entity.Node;
import com.devdo.node.repository.NodeRepository;
import com.devdo.roadmap.controller.dto.request.RoadmapRequestDto;
import com.devdo.roadmap.controller.dto.response.RoadmapDetailResponseDto;
import com.devdo.roadmap.controller.dto.response.RoadmapMainResponseDto;
import com.devdo.roadmap.controller.dto.response.RoadmapResponseDto;
import com.devdo.roadmap.entity.Roadmap;
import com.devdo.roadmap.repository.RoadmapRepository;
import com.devdo.roadmaptemplate.entity.TemplateNode;
import com.devdo.roadmaptemplate.entity.TemplateRoadmap;
import com.devdo.roadmaptemplate.repository.TemplateNodeRepository;
import com.devdo.roadmaptemplate.repository.TemplateRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final MemberRepository memberRepository;
    private final TemplateRoadmapRepository templateRoadmapRepository;
    private final TemplateNodeRepository templateNodeRepository;
    private final NodeRepository nodeRepository;

    @Transactional
    public Long createRoadmap(RoadmapRequestDto requestDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION, ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));

        Roadmap roadmap = Roadmap.builder()
                .title(requestDto.title())
                .member(member)
                .build();

        roadmapRepository.save(roadmap);
        return roadmap.getId();
    }

    @Transactional
    public Long createRoadmapFromTemplate(String roadmapTitle, String templateType, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION, ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));

        Roadmap newRoadmap = Roadmap.builder()
                .title(roadmapTitle)
                .member(member)
                .build();
        roadmapRepository.save(newRoadmap);

        // type에 맞는 템플릿 로드맵의 노드 조회
        TemplateRoadmap templateRoadmap = templateRoadmapRepository.findByType(templateType)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_TEMPLATE_NOT_FOUND_EXCEPTION, ErrorCode.ROADMAP_TEMPLATE_NOT_FOUND_EXCEPTION.getMessage()));

        List<TemplateNode> templateNodes = templateNodeRepository.findAllByTemplateRoadmapId(templateRoadmap.getId());

        Map<Long, Node> copiedNodesMap = new HashMap<>();

        // 템플릿 노드들을 복사하여 새로운 로드맵의 노드로 저장
        for (TemplateNode templateNode : templateNodes) {
            Node newNode = Node.builder()
                    .nodeName(templateNode.getNodeName())
                    .nodeShape(templateNode.getNodeShape())
                    .nodeColor(templateNode.getNodeColor())
                    .link(templateNode.getLink())
                    .roadmap(newRoadmap)
                    // .parentNode는 두 번째 루프에서 설정함
                    .build();

            // 새로운 노드를 저장하고 맵에 추가 (나중에 부모-자식 관계 설정용)
            Node savedNode = nodeRepository.save(newNode);
            copiedNodesMap.put(templateNode.getId(), savedNode);
        }

        // 모든 노드가 생성된 후 부모자식 관계 설정
        for (TemplateNode templateNode : templateNodes) {
            if (templateNode.getParentNodeId() != null) {
                Node newChildNode = copiedNodesMap.get(templateNode.getId());
                Node newParentNode = copiedNodesMap.get(templateNode.getParentNodeId());

                // 부모 노드 설정
                if (newParentNode != null) {
                    newParentNode.addChild(newChildNode);
                    nodeRepository.save(newParentNode);
                }
            }
        }

        return newRoadmap.getId();
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

    @Transactional(readOnly = true)
    public List<RoadmapMainResponseDto> getMainRoadmaps(Long memberId) {
        return roadmapRepository.findAllByMember_MemberId(memberId).stream()
                .map(r -> RoadmapMainResponseDto.builder()
                        .roadmapId(r.getId())
                        .title(r.getTitle())
                        .memberNickname(r.getMember().getNickname())
                        .createdAt(r.getCreatedAt())
                        .build()
                ).collect(toList());
    }

    @Transactional(readOnly = true)
    public RoadmapDetailResponseDto getRoadmapNodeDetail(Long roadmapId, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION, ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION.getMessage()));

        if (!roadmap.getMember().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION, ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }

        return RoadmapDetailResponseDto.from(roadmap);
    }

    @Transactional
    public void updateRoadmap(Long roadmapId, String newTitle, Long memberId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new IllegalArgumentException("해당 로드맵이 존재하지 않습니다."));
        roadmap.updateTitle(newTitle);
    }

    @Transactional
    public void deleteRoadmap(Long roadmapId, Long memberId) {
        roadmapRepository.deleteById(roadmapId);
    }
}
