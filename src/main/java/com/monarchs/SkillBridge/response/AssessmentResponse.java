package com.monarchs.SkillBridge.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentResponse {
    private Long id;
    private Long studentId;
    private JsonNode answers;
    private JsonNode scores;
    private JsonNode generatedProfile;
    private OffsetDateTime createdAt;
}
