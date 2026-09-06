package com.monarchs.SkillBridge.service;

import com.monarchs.SkillBridge.dto.StudentRegistrationDto;
import com.monarchs.SkillBridge.dto.StudentUpdateDto;
import com.monarchs.SkillBridge.response.StudentResponse;

public interface StudentService {

    StudentResponse getStudentDetailsService(Long id);

    String updateStudentService(Long id, StudentUpdateDto dto);
}
