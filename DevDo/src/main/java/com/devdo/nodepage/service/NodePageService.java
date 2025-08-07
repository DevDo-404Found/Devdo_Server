package com.devdo.nodepage.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.node.entity.Node;
import com.devdo.node.repository.NodeRepository;
import com.devdo.nodepage.controller.dto.request.NodePageRequestDto;
import com.devdo.nodepage.controller.dto.response.NodePageResponseDto;
import com.devdo.nodepage.entity.NodePage;
import com.devdo.nodepage.repository.NodePageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NodePageService {

    private final NodeRepository nodeRepository;
    private final NodePageRepository nodePageRepository;

    @Transactional
    public NodePageResponseDto create(Long nodeId, NodePageRequestDto request) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        NodePage nodePage = NodePage.builder()
                .node(node)
                .content(request.content())
                .emoji(request.emoji())
                .pictureUrl(request.pictureUrl())
                .build();

        NodePage saved = nodePageRepository.save(nodePage);

        return new NodePageResponseDto(
                saved.getNodePageId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getEmoji(),
                saved.getPictureUrl()
        );
    }

    @Transactional(readOnly = true)
    public NodePageResponseDto get(Long nodeId) {
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        return new NodePageResponseDto(
                nodePage.getNodePageId(),
                nodePage.getTitle(),
                nodePage.getContent(),
                nodePage.getEmoji(),
                nodePage.getPictureUrl()
        );
    }

    @Transactional
    public NodePageResponseDto update(Long nodeId, NodePageRequestDto request) {
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        nodePage.updateContent(request.content());
        nodePage.updateEmoji(request.emoji());
        nodePage.updatePictureUrl(request.pictureUrl());

        return new NodePageResponseDto(
                nodePage.getNodePageId(),
                nodePage.getTitle(),
                nodePage.getContent(),
                nodePage.getEmoji(),
                nodePage.getPictureUrl()
        );
    }

    @Transactional
    public void delete(Long nodeId) {
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        nodePageRepository.delete(nodePage);
    }
}
