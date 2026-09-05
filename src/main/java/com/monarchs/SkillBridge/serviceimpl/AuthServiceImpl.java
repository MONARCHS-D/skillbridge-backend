package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.LoginRequestDto;
import com.monarchs.SkillBridge.entities.User;
import com.monarchs.SkillBridge.repository.UserRepository;
import com.monarchs.SkillBridge.response.LoginResponse;
import com.monarchs.SkillBridge.secutity.JwtUtil;
import com.monarchs.SkillBridge.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    private final AuthenticationManager authManager;

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepo, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse loginService(LoginRequestDto loginRequestDto) {
        Authentication authentication= authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        User user=userRepo.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken= jwtUtil.createJwtToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(accessToken)
                .role(user.getRole().name())
                .build();
    }

    @Override
    public String logoutService(HttpServletRequest request) {
        String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null || !authHeader.startsWith("Bearer ")) throw new RuntimeException("Token missing");
        log.info("Token with Bearer {}",authHeader);
        String jwt=authHeader.substring(7);

        try{
            jwtUtil.parseClaims(jwt);
            return "You have logged out successfully";
        } catch (ExpiredJwtException e) {
            return "Token already expired";

        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
