package com.monarchs.SkillBridge.dto;

import lombok.*;
import tools.jackson.databind.JsonNode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDto {

    private String phone;

    private Long institution;

    private String resume;

    private JsonNode skills;

    private JsonNode certifications;

    private JsonNode projects;
}
