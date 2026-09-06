package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentRequest {

    @NotNull
    private JsonNode answers;
}
