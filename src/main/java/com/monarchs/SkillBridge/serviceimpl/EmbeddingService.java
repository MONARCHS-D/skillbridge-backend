package com.monarchs.SkillBridge.serviceimpl;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Iterator;
import java.util.Map;

@Service
public class EmbeddingService {

    private static final int EMBEDDING_DIMENSION = 384;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public EmbeddingService(
            @Qualifier("embeddingRestClient") RestClient restClient,
            ObjectMapper objectMapper
    ) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public float[] generateEmbedding(String text) {

        validateText(text);

        EmbeddingResponse response = restClient.post()
                .uri("/embed")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new EmbeddingRequest(text))
                .retrieve()
                .body(EmbeddingResponse.class);

        if (response == null || response.embedding() == null) {
            throw new IllegalStateException(
                    "Embedding service returned no embedding"
            );
        }

        float[] embedding = response.embedding();

        validateEmbedding(embedding);

        return embedding;
    }

    public String buildStudentProfileText(
            String name,
            JsonNode skills,
            JsonNode certifications,
            JsonNode projects
    ) {

        return """
                Student Profile

                Name: %s

                Skills: %s

                Certifications: %s

                Projects: %s
                """.formatted(
                safeValue(name),
                jsonToText(skills),
                jsonToText(certifications),
                jsonToText(projects)
        ).trim();
    }

    public String buildPostingProfileText(
            String title,
            String description,
            String location,
            Boolean isRemote,
            String stipend,
            String employmentType,
            JsonNode requiredSkills,
            JsonNode metadata
    ) {

        return """
                Job Posting

                Title: %s

                Description: %s

                Location: %s

                Remote: %s

                Stipend: %s

                Employment Type: %s

                Required Skills: %s

                Additional Information: %s
                """.formatted(
                safeValue(title),
                safeValue(description),
                safeValue(location),
                Boolean.TRUE.equals(isRemote) ? "Yes" : "No",
                safeValue(stipend),
                safeValue(employmentType),
                jsonToText(requiredSkills),
                jsonToText(metadata)
        ).trim();
    }

    private String jsonToText(JsonNode node) {

        if (node == null || node.isNull()) {
            return "";
        }

        // FIX: Jackson 3's ObjectMapper throws tools.jackson.core.JacksonException,
        // which is UNCHECKED (extends RuntimeException) — not the old checked
        // com.fasterxml JsonProcessingException. Catching it here is now optional
        // (compiler won't force it), but kept so we can wrap it into our own
        // IllegalStateException with a clearer message.
        try {

            JsonNode normalizedNode =
                    objectMapper.readTree(
                            objectMapper.writeValueAsString(node)
                    );

            StringBuilder text = new StringBuilder();

            appendJsonText(normalizedNode, text);

            return text.toString().trim();

        } catch (JacksonException e) {

            throw new IllegalStateException(
                    "Failed to convert JSON data into embedding text", e
            );
        }
    }

    private void appendJsonText(
            JsonNode node,
            StringBuilder text
    ) {

        if (node == null || node.isNull()) {
            return;
        }

        if (node.isValueNode()) {

            String value = node.asText().trim();

            if (!value.isEmpty()) {
                appendSeparator(text);
                text.append(value);
            }

            return;
        }

        if (node.isArray()) {

            for (JsonNode child : node) {
                appendJsonText(child, text);
            }

            return;
        }

        if (node.isObject()) {

            Iterator<Map.Entry<String, JsonNode>> fields =
                    node.properties().iterator();

            while (fields.hasNext()) {

                Map.Entry<String, JsonNode> field =
                        fields.next();

                String fieldName = field.getKey();
                JsonNode fieldValue = field.getValue();

                if (fieldValue == null || fieldValue.isNull()) {
                    continue;
                }

                if (fieldValue.isValueNode()) {

                    String value = fieldValue.asText().trim();

                    if (!value.isEmpty()) {

                        appendSeparator(text);

                        text.append(fieldName)
                                .append(": ")
                                .append(value);
                    }

                } else {

                    StringBuilder nestedText =
                            new StringBuilder();

                    appendJsonText(fieldValue, nestedText);

                    if (nestedText.length() > 0) {

                        appendSeparator(text);

                        text.append(fieldName)
                                .append(": ")
                                .append(nestedText);
                    }
                }
            }
        }
    }

    private void appendSeparator(StringBuilder text) {

        if (text.length() > 0) {
            text.append(", ");
        }
    }

    private String safeValue(String value) {

        return value == null ? "" : value.trim();
    }

    private void validateText(String text) {

        if (text == null || text.isBlank()) {

            throw new IllegalArgumentException(
                    "Text for embedding cannot be null or blank"
            );
        }
    }

    private void validateEmbedding(float[] embedding) {

        if (embedding == null) {

            throw new IllegalStateException(
                    "Embedding model returned null"
            );
        }

        if (embedding.length != EMBEDDING_DIMENSION) {

            throw new IllegalStateException(
                    "Expected embedding dimension "
                            + EMBEDDING_DIMENSION
                            + " but got "
                            + embedding.length
            );
        }

        for (float value : embedding) {

            if (Float.isNaN(value) ||
                    Float.isInfinite(value)) {

                throw new IllegalStateException(
                        "Embedding contains NaN or Infinity"
                );
            }
        }
    }

    private record EmbeddingRequest(String text) {
    }

    private record EmbeddingResponse(float[] embedding) {
    }
}