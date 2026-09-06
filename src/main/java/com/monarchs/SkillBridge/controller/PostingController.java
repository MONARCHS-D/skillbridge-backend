package com.monarchs.SkillBridge.controller;

import com.monarchs.SkillBridge.dto.PostingDto;
import com.monarchs.SkillBridge.response.ApiResponse;
import com.monarchs.SkillBridge.service.PostingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v6/skillbridge/company/posting")
public class PostingController {

    private final PostingService postingService;

    public PostingController(PostingService postingService) {
        this.postingService = postingService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addPostingController(Authentication authentication, @RequestBody @Valid PostingDto dto){
        Long id=Long.valueOf(authentication.getName());
        String response= postingService.addPostingService(id,dto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED,response));
    }
}
