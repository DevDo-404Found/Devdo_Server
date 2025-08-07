package com.devdo.node.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.node.controller.dto.response.NodeDetailResponseDto;
import com.devdo.node.controller.dto.response.NodeResponseDto;
import com.devdo.node.entity.Node;
import com.devdo.node.repository.NodeRepository;
import com.devdo.roadmap.entity.Roadmap;
import com.devdo.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeService {

    private final NodeRepository nodeRepository;
    private final MemberRepository memberRepository;
    private final RoadmapRepository roadmapRepository;

    // 공통 메서드
    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }
        try {
            Long memberId = Long.parseLong(principal.getName());
            return findMemberById(memberId);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }
    }

    // 주요 서비스 메서드
    public NodeDetailResponseDto createNode(Principal principal, NodeDetailResponseDto nodeDetailResponseDto) {
        Member member = getMemberFromPrincipal(principal);

        Roadmap roadmap = roadmapRepository.findById(nodeDetailResponseDto.roadmapId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION, ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION.getMessage()));

        Node parent = null;
        if (nodeDetailResponseDto.parentNodeId() != null) {
            parent = nodeRepository.findById(nodeDetailResponseDto.parentNodeId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));
        }

        Node node = Node.builder()
                .nodeName(nodeDetailResponseDto.nodeName())
                .nodeShape(nodeDetailResponseDto.nodeShape())
                .link(nodeDetailResponseDto.link())
                .pictureUrl(nodeDetailResponseDto.pictureUrl())
                .roadmap(roadmap)
                .parentNode(parent)
                .build();

        return NodeDetailResponseDto.from(nodeRepository.save(node));
    }

    @Transactional(readOnly = true)
    public NodeResponseDto getNodeDetail(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        return NodeResponseDto.from(node);
    }

    public NodeDetailResponseDto updateNode(Principal principal, Long nodeId, NodeDetailResponseDto nodeDetailResponseDto) {
        Member member = getMemberFromPrincipal(principal);

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        Roadmap roadmap = roadmapRepository.findById(nodeDetailResponseDto.roadmapId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION, ErrorCode.ROADMAP_NOT_FOUND_EXCEPTION.getMessage()));

        Node parentNode = null;
        if (nodeDetailResponseDto.parentNodeId() != null) {
            parentNode = nodeRepository.findById(nodeDetailResponseDto.parentNodeId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));
        }

        node.updateNode(
                roadmap,
                nodeDetailResponseDto.nodeName(),
                nodeDetailResponseDto.nodeShape(),
                nodeDetailResponseDto.nodeColor(),
                nodeDetailResponseDto.pictureUrl(),
                nodeDetailResponseDto.link(),
                parentNode
        );

        return NodeDetailResponseDto.from(node);
    }


    public void deleteNode(Principal principal, Long nodeId) {
        Member member = getMemberFromPrincipal(principal);
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        nodeRepository.delete(node);
    }
}