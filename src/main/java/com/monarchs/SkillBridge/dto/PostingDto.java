package com.monarchs.SkillBridge.dto;

import lombok.*;
import tools.jackson.databind.JsonNode;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostingDto {

    private String title;

    private String description;

    private String location;

    private String stipend;

    private String employmentType;

    private JsonNode requiredSkills;

    private JsonNode metadata;

    private OffsetDateTime deadline;
}
