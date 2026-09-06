package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResendOtpDto {

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Enter valid email")
    private String email;

}
