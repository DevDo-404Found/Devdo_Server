package com.devdo.node.controller.dto.response;

import com.devdo.node.entity.Node;
import com.devdo.node.entity.NodeShape;

public record NodeResponseDto(
        String nodeName,
        NodeShape nodeShape
) {
    public static NodeResponseDto from(Node node) {
        return new NodeResponseDto(
                node.getNodeName(),
                node.getNodeShape()
        );
    }
}
