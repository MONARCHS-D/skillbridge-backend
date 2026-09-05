package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.dto.InstitutionRegistrationDto;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InstitutionRegistration implements RegistrationStrategy{

    private final InstitutionRepository institutionRepo;

    public InstitutionRegistration(InstitutionRepository institutionRepo) {
        this.institutionRepo = institutionRepo;
    }

    @Override
    public UserRole roleResolver() {
        return UserRole.INSTITUTION;
    }

    @Transactional
    @Override
    public void createProfile(BaseUserDto baseUserDto, User user) {
        InstitutionRegistrationDto dto=(InstitutionRegistrationDto) baseUserDto;
        Institution institution=Institution.builder()
                .user(user)
                .name(dto.getName())
                .address(dto.getAddress())
                .build();
        institutionRepo.save(institution);
    }
}
