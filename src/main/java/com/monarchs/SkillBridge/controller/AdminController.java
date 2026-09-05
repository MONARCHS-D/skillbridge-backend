package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v3/skillbridge/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approveUserController(@PathVariable Long id){
        String response= adminService.approveUserService(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectUserController(@PathVariable Long id){
        String response= adminService.rejectUserService(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
