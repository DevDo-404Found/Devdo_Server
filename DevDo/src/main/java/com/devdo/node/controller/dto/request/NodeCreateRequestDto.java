package com.devdo.node.controller.dto.request;

import com.devdo.node.entity.NodeShape;

public record NodeCreateRequestDto(
        String nodeName,
        NodeShape nodeShape,
        String imageUrl,
        String link
) {
}
