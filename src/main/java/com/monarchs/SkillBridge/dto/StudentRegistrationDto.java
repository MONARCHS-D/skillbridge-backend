package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRegistrationDto extends BaseUserDto {
    @NotNull
    private Long institutionId;

    @NotBlank
    private String name;

    private String resumeUrl;

    private JsonNode skills;

    private JsonNode certifications;

    private JsonNode projects;
}
