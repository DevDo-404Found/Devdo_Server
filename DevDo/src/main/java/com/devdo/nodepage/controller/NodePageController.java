package com.devdo.nodepage.controller;

import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import com.devdo.nodepage.controller.dto.request.NodePageRequestDto;
import com.devdo.nodepage.controller.dto.response.NodePageResponseDto;
import com.devdo.nodepage.service.NodePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roadmap/node/detail")
@RequiredArgsConstructor
@Tag(name = "노드 상세 페이지 API", description = "Node Detail Page 관련 API들 입니다. 노드 상세페이지 삭제는 노드를 삭제하면 함께 삭제됩니다.")
public class NodePageController {

    private final NodePageService nodePageService;

    @PostMapping("/{nodeId}")
    @Operation(method = "POST", summary = "노드 상세 페이지 추가", description = "노드 상세 페이지를 추가합니다. (노드 1개당 1개의 페이지만 추가 가능합니다.")
    public ApiResTemplate<NodePageResponseDto> createNodePage(
            @PathVariable Long nodeId,
            @RequestBody NodePageRequestDto request
    ) {
        NodePageResponseDto response = nodePageService.create(nodeId, request);
        return ApiResTemplate.successResponse(SuccessCode.NODE_SAVE_SUCCESS, response);
    }

    @GetMapping("/{nodeId}")
    @Operation(method = "GET", summary = "노드 상세 페이지 조회", description = "노드 상세 페이지를 조회합니다.")
    public ApiResTemplate<NodePageResponseDto> getNodePage(@PathVariable Long nodeId) {
        NodePageResponseDto response = nodePageService.get(nodeId);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, response);
    }
    @PutMapping("/{nodeId}")
    @Operation(method = "PUT", summary = "노드 상세 페이지 수정", description = "노드 상세 페이지를 수정합니다.")
    public ApiResTemplate<NodePageResponseDto> updateNodePage(
            @PathVariable Long nodeId,
            @RequestBody NodePageRequestDto request
    ) {
        NodePageResponseDto response = nodePageService.update(nodeId, request);
        return ApiResTemplate.successResponse(SuccessCode.NODE_UPDATE_SUCCESS, response);
    }
}
