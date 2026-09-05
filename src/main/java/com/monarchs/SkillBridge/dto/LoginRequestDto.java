package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {
    @Email(message = "Enter valid email")
    private String email;

    @NotBlank(message = "Enter your password")
    private String password;
}
