package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.response.ActivityRegistrationResponse;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.ActivityRegistrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v10/skillbridge/activity")
public class ActivityRegistrationController {

    private final ActivityRegistrationService registrationService;

    public ActivityRegistrationController(ActivityRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{activityId}/register")
    public ResponseEntity<ApiResponse<ActivityRegistrationResponse>> activityRegistrationController(@PathVariable Long activityId, Authentication authentication){
        Long userId=Long.valueOf(authentication.getName());
        ActivityRegistrationResponse response=registrationService.registerToActivity(activityId,userId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/{id}/registrations")
    public ResponseEntity<ApiResponse<Page<ActivityRegistrationResponse>>> activityRegistrations(@PathVariable Long id,
                                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                                 @RequestParam(defaultValue = "10") int size){
        Page<ActivityRegistrationResponse> responses=registrationService.listRegistrationsForActivity(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }

    @GetMapping("/me/registrations")
    public ResponseEntity<ApiResponse<Page<ActivityRegistrationResponse>>> myRegistrations(Authentication authentication,
                                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                                 @RequestParam(defaultValue = "10") int size){
        Long id=Long.valueOf(authentication.getName());
        Page<ActivityRegistrationResponse> responses=registrationService.listRegistrationsForUser(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }
}
