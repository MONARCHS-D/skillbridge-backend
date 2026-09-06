package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.projection.InstitutionDropdownView;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v4/skillbridge/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstitutionDropdownView>>> getAllInstitutionController(){
        List<InstitutionDropdownView> institutions=institutionService.getAllInstitutionService();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,institutions));
    }
}
