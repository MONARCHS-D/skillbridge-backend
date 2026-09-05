package com.monarchs.SkillBridge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUserDto {
    @NotBlank(message = "Write Your Email")
    @Email
    private String email;

    @NotBlank(message = "username can't empty")
    @Size(min=4, max= 30, message = "username length must be greater than 4")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, max = 20 , message = "Password length must be greater than 8")
    private String password;

    @NotBlank(message = "Phone Number can't be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter a valid phone number")
    private String phone;
}
