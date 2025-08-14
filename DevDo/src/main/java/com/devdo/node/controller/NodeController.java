package com.devdo.node.controller;

import com.devdo.node.controller.dto.response.NodeDetailResponseDto;
import com.devdo.node.controller.dto.response.NodeResponseDto;
import com.devdo.node.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roadmap/node")
@Tag(name = "노드 API", description = "Node 관련 API들 입니다.")
public class NodeController {

    private final NodeService nodeService;

    @PostMapping
    @Operation(method = "POST", summary = "노드 추가", description = "노드를 추가합니다. (부모노드가 없을 시 parentNodeId에 null을 입력해주세요!)")
    public ResponseEntity<NodeDetailResponseDto> createNode(
            Principal principal,
            @RequestBody NodeDetailResponseDto nodeDetailResponseDto
    ) {
        return ResponseEntity.ok(nodeService.createNode(principal, nodeDetailResponseDto));
    }

    @GetMapping("/{nodeId}")
    @Operation(method = "GET", summary = "노드 조회", description = "노드를 조회합니다.")
    public ResponseEntity<NodeResponseDto> getNodeDetail(
            @PathVariable Long nodeId
    ) {
        return ResponseEntity.ok(nodeService.getNodeDetail(nodeId));
    }

    @PutMapping("/{nodeId}")
    @Operation(method = "PUT", summary = "노드 수정", description = "노드를 수정합니다. (부모노드가 없을 시 parentNodeId에 null을 입력해주세요!)")
    public ResponseEntity<NodeDetailResponseDto> updateNode(
            Principal principal,
            @PathVariable Long nodeId,
            @RequestBody NodeDetailResponseDto nodeDetailResponseDto
    ) {
        return ResponseEntity.ok(nodeService.updateNode(principal, nodeId, nodeDetailResponseDto));
    }

    @DeleteMapping("/{nodeId}")
    @Operation(method = "DEIETE", summary = "노드 삭제", description = "노드를 삭제합니다.")
    public ResponseEntity<Void> deleteNode(
            Principal principal,
            @PathVariable Long nodeId
    ) {
        nodeService.deleteNode(principal, nodeId);
        return ResponseEntity.noContent().build();
    }
}
