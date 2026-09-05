package com.monarchs.SkillBridge.dto;

import tools.jackson.databind.JsonNode;
import com.monarchs.SkillBridge.enums.Designation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademicianRegistrationDto extends BaseUserDto {

    @NotNull
    private Long institutionId;

    @NotBlank(message = "Enter your name")
    private String name;
    
    private Designation designation;

    private JsonNode areaOfInterest;
}
