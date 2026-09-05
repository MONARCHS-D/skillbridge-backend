package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstitutionRegistrationDto extends BaseUserDto{

    @NotBlank(message = "Enter institution name")
    private String name;

    @NotBlank(message = "Enter institution address")
    private String address;
}
