package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.StudentUpdateDto;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.response.StudentResponse;
import com.monarchs.SkillBridge.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v5/skillbridge/user/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentDetailsController(Authentication authentication){
        Long id=Long.valueOf(authentication.getName());
        StudentResponse response=studentService.getStudentDetailsService(id);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateStudentController(Authentication authentication,@RequestBody StudentUpdateDto dto){
        Long id=Long.valueOf(authentication.getName());
        String response=studentService.updateStudentService(id,dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,response));
    }
}
