package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.enums.UserStatus;
import com.monarchs.SkillBridge.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(email).orElseThrow(()->new RuntimeException("Invalid user"));
        if(user.getStatus()!=UserStatus.ACTIVE){
            throw new UsernameNotFoundException("User is not approved");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
