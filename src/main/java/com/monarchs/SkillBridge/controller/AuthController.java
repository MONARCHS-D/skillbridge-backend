package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.LoginRequestDto;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.response.LoginResponse;
import com.monarchs.SkillBridge.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/skillbridge/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginController(@RequestBody @Valid LoginRequestDto dto){
        LoginResponse response=authService.loginService(dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutController(HttpServletRequest request){
        String response=authService.logoutService(request);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
