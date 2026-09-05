package com.monarchs.SkillBridge.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.monarchs.SkillBridge.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempUserRegistrationDto {
    private String tempOtp;
    private LocalDateTime expiryTime;
    private UserRole role;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private BaseUserDto baseUserDto;
}
