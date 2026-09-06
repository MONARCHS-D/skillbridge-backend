package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.ActivityDto;
import com.monarchs.SkillBridge.response.ActivityResponse;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v9/skillbridge/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ActivityResponse>> createActivityController(Authentication authentication, @RequestBody @Valid ActivityDto dto){
        Long id=Long.valueOf(authentication.getName());
        ActivityResponse response=activityService.createActivity(id,dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED,response));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ActivityResponse>> updateActivityController(@PathVariable Long id, @RequestBody @Valid ActivityDto dto){
        ActivityResponse response=activityService.updateActivity(id,dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteActivityController(@PathVariable Long id){
        String response=activityService.deleteActivity(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/getActivity/{id}")
    public ResponseEntity<ApiResponse<ActivityResponse>> getActivityByIdController(@PathVariable Long id){
        ActivityResponse response=activityService.getActivity(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<ApiResponse<Page<ActivityResponse>>> listActivitiesForCompany(@PathVariable Long id,
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "10") int size){
        Page<ActivityResponse> responses=activityService.listActivitiesForCompany(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }

    @GetMapping("/institution/{id}")
    public ResponseEntity<ApiResponse<Page<ActivityResponse>>> listActivitiesForInstitution(@PathVariable Long id,
                                                                                        @RequestParam(defaultValue = "0") int page,
                                                                                        @RequestParam(defaultValue = "10") int size){
        Page<ActivityResponse> responses=activityService.listActivitiesForInstitution(id, PageRequest.of(page,size));
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,responses));
    }
}
