package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.LoginRequestDto;
import com.monarchs.SkillBridge.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse loginService(LoginRequestDto loginRequestDto);

    String logoutService(HttpServletRequest request);
}
