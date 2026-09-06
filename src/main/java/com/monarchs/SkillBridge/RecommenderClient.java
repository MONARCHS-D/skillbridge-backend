package com.monarchs.SkillBridge;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
public class RecommenderClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    public RecommenderClient(@Qualifier("recommenderRestClient") RestClient restClient, @Value("${recommender.service.url}") String baseUrl, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.objectMapper = objectMapper;
    }

    public float[] embedText(String text) {
        Map<String, Object> requestBody = Map.of("text", text);
        try {
            Map<String, Object> response = restClient
                    .post()
                    .uri(baseUrl + "/embed")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
            if (response == null || response.get("embedding") == null) {
                throw new RecommenderException("Embedding service returned empty embedding");
            }
            return toFloatArray(response.get("embedding"));
        } catch (Exception e) {
            throw new RecommenderException("Failed to generate embedding: " + e.getMessage(), e);
        }
    }


    public ParseResumeResponse parseResumeFromUrl(String resumeUrl) {
        Map<String, Object> requestBody = Map.of("resume_url", resumeUrl);
        try {
            Map<String, Object> response = restClient
                    .post()
                    .uri(baseUrl + "/parse_resume")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
            if (response == null) {
                throw new RecommenderException("Resume parser returned empty response");
            }
            JsonNode skills = objectMapper.valueToTree(response.get("skills"));
            float[] embedding = toFloatArray(response.get("embedding"));
            String textSnippet = response.get("text_snippet") != null ? response.get("text_snippet").toString() : null;
            return new ParseResumeResponse(skills, embedding, textSnippet);
        } catch (Exception e) {
            throw new RecommenderException("Failed to parse resume: " + e.getMessage(), e);
        }
    }


    public Map<String, Object> assess(JsonNode answers) {
        Map<String, Object> requestBody = Map.of("answers", objectMapper.convertValue(answers, Map.class));
        try {
            return restClient
                    .post()
                    .uri(baseUrl + "/assess")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);
        } catch (Exception e) {
            throw new RecommenderException("Failed to assess answers: " + e.getMessage(), e);
        }
    }


    public List<Map<String, Object>> recommend(Map<String, Object> requestBody) {
        try {
            return restClient
                    .post()
                    .uri(baseUrl + "/recommend")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(List.class);
        } catch (Exception e) {
            throw new RecommenderException("Failed to get recommendations: " + e.getMessage(), e);
        }
    }

    private float[] toFloatArray(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof List<?> list) {
            float[] result = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Object element = list.get(i);
                if (element instanceof Number number) {
                    result[i] = number.floatValue();
                } else {
                    result[i] = Float.parseFloat(element.toString());
                }
            }
            return result;
        }
        JsonNode node = objectMapper.valueToTree(value);
        if (node.isArray()) {
            float[] result = new float[node.size()];
            for (int i = 0; i < node.size(); i++) {
                result[i] = (float) node.get(i).asDouble();
            }
            return result;
        }
        throw new IllegalArgumentException("Invalid embedding format");
    }

    @Getter
    public static class ParseResumeResponse {
        private final JsonNode skills;
        private final float[] embedding;
        private final String textSnippet;

        public ParseResumeResponse(JsonNode skills, float[] embedding, String textSnippet) {
            this.skills = skills;
            this.embedding = embedding;
            this.textSnippet = textSnippet;
        }

    }

    public static class RecommenderException extends RuntimeException {
        public RecommenderException(String message) {
            super(message);
        }

        public RecommenderException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
