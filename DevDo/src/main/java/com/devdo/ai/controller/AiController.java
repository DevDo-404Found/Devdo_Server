package com.devdo.ai.controller;


import com.devdo.ai.controller.dto.AiRequestDto;
import com.devdo.ai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/roadmap")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/node/detail/{nodeId}")
    public ResponseEntity<String> generateContent(
            @PathVariable Long nodeId,
            @RequestParam String queryType) {
        String result = aiService.generateContentByNode(nodeId, queryType);
        return ResponseEntity.ok(result);
    }
}

