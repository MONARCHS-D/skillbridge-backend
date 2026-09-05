package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.dto.OtpVerifyDto;
import com.monarchs.SkillBridge.enums.UserRole;

public interface UserRegistrationService {

    String initiateUserRegistrationService(BaseUserDto dto, UserRole role);

    String finalUserRegistrationService(OtpVerifyDto dto);

}
