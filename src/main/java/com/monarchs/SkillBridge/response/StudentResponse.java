package com.monarchs.SkillBridge.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.JsonNode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {

    private String name;

    private String phone;

    private String institution;

    private String resume;

    private JsonNode skills;

    private JsonNode certifications;

    private JsonNode projects;
}
