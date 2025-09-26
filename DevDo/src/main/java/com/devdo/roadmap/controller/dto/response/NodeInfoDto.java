package com.devdo.roadmap.controller.dto.response;

import com.devdo.node.entity.Node;
import com.devdo.node.entity.NodeColor;
import com.devdo.node.entity.NodeShape;

public record NodeInfoDto(
        Long nodeId,
        String nodeName,
        NodeColor nodeColor,
        NodeShape nodeShape
) {
    public static NodeInfoDto from(Node node) {
        return new NodeInfoDto(
                node.getNodeId(),
                node.getNodeName(),
                node.getNodeColor(),
                node.getNodeShape()
        );
    }
}