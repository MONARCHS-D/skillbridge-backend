package com.monarchs.SkillBridge.strategy;

import com.monarchs.SkillBridge.dto.BaseUserDto;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleRegistrationDispatcher {

    private final Map<UserRole,RegistrationStrategy> handlerMap;

    public RoleRegistrationDispatcher(List<RegistrationStrategy> handlers) {
        this.handlerMap=handlers.stream()
                .collect(Collectors.toMap(RegistrationStrategy::roleResolver,h->h));
    }

    public void completeRegistration(UserRole role, BaseUserDto baseUserDto, User user){
        RegistrationStrategy strategy=handlerMap.get(role);
        if(strategy==null){
            throw new RuntimeException("No role found");
        }
        strategy.createProfile(baseUserDto, user);
    }
}
