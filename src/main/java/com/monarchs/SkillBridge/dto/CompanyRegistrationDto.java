package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRegistrationDto extends BaseUserDto {
    @NotBlank
    private String name;

    @NotBlank(message = "Website link is mandatory")
    private String website;

    @Email
    private String contactEmail;

    @NotBlank(message = "Phone Number can't be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid phone number")
    private String contactPhone;
}
