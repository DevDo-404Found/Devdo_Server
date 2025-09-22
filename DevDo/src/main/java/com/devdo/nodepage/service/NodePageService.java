package com.devdo.nodepage.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.global.s3.S3Service;
import com.devdo.node.entity.Node;
import com.devdo.node.repository.NodeRepository;
import com.devdo.nodepage.controller.dto.request.NodePageRequestDto;
import com.devdo.nodepage.controller.dto.response.NodePageResponseDto;
import com.devdo.nodepage.entity.NodePage;
import com.devdo.nodepage.repository.NodePageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NodePageService {

    private final NodeRepository nodeRepository;
    private final NodePageRepository nodePageRepository;
    private final S3Service s3Service;

    @Transactional
    public NodePageResponseDto create(Long nodeId, NodePageRequestDto request) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        String pictureUrl = null;
        if (request.pictureFile() != null && !request.pictureFile().isEmpty()) {
            pictureUrl = s3Service.uploadFile(request.pictureFile(), "nodepage-pictures");
        }
        NodePage nodePage = NodePage.builder()
                .node(node)
                .content(request.content())
                .emoji(request.emoji())
                .pictureUrl(pictureUrl)
                .build();

        NodePage saved = nodePageRepository.save(nodePage);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = false)
    public NodePageResponseDto getOrCreate(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        // NodePage 존재 여부 확인
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseGet(() -> {
                    // 없으면 기본 값으로 생성
                    NodePage newPage = NodePage.builder()
                            .node(node)
                            .content("")
                            .emoji("")
                            .pictureUrl(null)
                            .build();
                    return nodePageRepository.save(newPage);
                });

        return mapToResponse(nodePage);
    }

    @Transactional
    public NodePageResponseDto update(Long nodeId, NodePageRequestDto request) {
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        if (request.pictureFile() != null && !request.pictureFile().isEmpty()) {
            // 기존 파일 삭제 후 새 업로드
            if (nodePage.getPictureUrl() != null) {
                s3Service.deleteFile(nodePage.getPictureUrl());
            }
            String newUrl = s3Service.uploadFile(request.pictureFile(), "nodepage-pictures");
            nodePage.updatePictureUrl(newUrl);
        }

        nodePage.updateContent(request.content());
        nodePage.updateEmoji(request.emoji());

        return mapToResponse(nodePage);
    }

    @Transactional
    public void delete(Long nodeId) {
        NodePage nodePage = nodePageRepository.findByNode_NodeId(nodeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NODE_NOT_FOUND_EXCEPTION, ErrorCode.NODE_NOT_FOUND_EXCEPTION.getMessage()));

        nodePageRepository.delete(nodePage);
    }

    private NodePageResponseDto mapToResponse(NodePage nodePage) {
        return new NodePageResponseDto(
                nodePage.getNodePageId(),
                nodePage.getTitle(),
                nodePage.getContent(),
                nodePage.getEmoji(),
                nodePage.getPictureUrl()
        );
    }

    @Scheduled(cron = "0 0 3 * * *") // 매일 3시 0분에 실행
    @Transactional
    public void deleteUnusedImages() {
        // 24시간 전에 생성되었으며 노드와 연결되지 않은 NodePage를 찾음
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<NodePage> orphanedPages = nodePageRepository
                .findByPictureUrlIsNotNullAndNodeIsNullAndCreatedAtBefore(twentyFourHoursAgo);

        for (NodePage page : orphanedPages) {
            s3Service.deleteFile(page.getPictureUrl()); // S3에서 이미지 파일 삭제
            nodePageRepository.delete(page); // DB에서 NodePage 레코드 삭제
        }
    }
}
