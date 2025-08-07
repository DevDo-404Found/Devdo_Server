package com.devdo.node.controller.dto.response;

import com.devdo.node.entity.Node;
import com.devdo.node.entity.NodeColor;
import com.devdo.node.entity.NodeShape;

public record NodeDetailResponseDto(
        Long roadmapId,
        String nodeName,
        NodeShape nodeShape,
        NodeColor nodeColor,
        String pictureUrl,
        String link,
        Long parentNodeId
) {
    public static NodeDetailResponseDto from(Node node) {
        return new NodeDetailResponseDto(
                node.getRoadmap().getId(),
                node.getNodeName(),
                node.getNodeShape(),
                node.getNodeColor(),
                node.getPictureUrl(),
                node.getLink(),
                node.getParentNode() != null ? node.getParentNode().getNodeId() : null
        );
    }
}
