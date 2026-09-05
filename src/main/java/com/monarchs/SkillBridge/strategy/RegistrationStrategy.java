package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.enums.UserStatus;

public interface RegistrationStrategy {
    UserRole roleResolver();
    void createProfile(BaseUserDto baseUserDto, User user);
}
