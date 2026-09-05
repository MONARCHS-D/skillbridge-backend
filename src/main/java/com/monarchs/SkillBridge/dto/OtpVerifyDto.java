package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerifyDto {

    @Email
    private String email;

    @NotBlank(message = "Enter 6 digit otp")
    private String otp;
}
