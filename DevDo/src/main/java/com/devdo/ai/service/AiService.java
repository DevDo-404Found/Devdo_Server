package com.devdo.ai.service;

import com.devdo.node.entity.Node;
import com.devdo.node.repository.NodeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

    private final NodeRepository nodeRepository;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=%s";

    public AiService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public String generateContentByNode(Long nodeId, String queryType) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node를 찾을 수 없습니다."));

        String roadmapTitle = node.getRoadmap().getTitle();
        String nodeName = node.getNodeName();

        String subject = roadmapTitle + " / " + nodeName;

        String prompt = switch (queryType) {
            case "lecture" -> "✨ '" + subject + "' 관련 인터넷 강의를 추천해줘.";
            case "article" -> "☘️ '" + subject + "' 관련 개념을 설명해 주는 아티클을 추천해줘.";
            case "curriculum" -> "🎁 '" + subject + "' 공부하기 좋은 커리큘럼을 작성해줘.";
            default -> "✨ '" + subject + "' 관련 정보를 알려줘.";
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> message = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);
        String url = String.format(GEMINI_API_URL, apiKey);

        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);
            JsonNode candidates = response.getBody().path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("content").path("parts").get(0).path("text").asText();
            } else {
                return "정보를 가져오지 못했습니다. 다시 시도해 주세요.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "AI 요청 중 오류 발생: " + e.getMessage();
        }
    }
}