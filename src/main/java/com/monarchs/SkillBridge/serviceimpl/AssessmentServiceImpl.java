package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.RecommenderClient;
import com.monarchs.SkillBridge.dto.AssessmentRequest;
import com.monarchs.SkillBridge.entities.Assessment;
import com.monarchs.SkillBridge.entities.RecommendationLog;
import com.monarchs.SkillBridge.entities.Student;
import com.monarchs.SkillBridge.repository.AssessmentRepository;
import com.monarchs.SkillBridge.repository.RecommendationLogRepository;
import com.monarchs.SkillBridge.repository.StudentRepository;
import com.monarchs.SkillBridge.response.AssessmentResponse;
import com.monarchs.SkillBridge.service.AssessmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepo;
    private final StudentRepository studentRepo;
    private final RecommenderClient recommenderClient;
    private final RecommendationLogRepository recommendationLogRepo;
    private final ObjectMapper objectMapper;

    public AssessmentServiceImpl(AssessmentRepository assessmentRepo, StudentRepository studentRepo, RecommenderClient recommenderClient, RecommendationLogRepository recommendationLogRepo, ObjectMapper objectMapper) {
        this.assessmentRepo = assessmentRepo;
        this.studentRepo = studentRepo;
        this.recommenderClient = recommenderClient;
        this.recommendationLogRepo = recommendationLogRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public AssessmentResponse submitAssessment(Long id, AssessmentRequest request) {
        Student student=studentRepo.findByUserId(id)
                .orElseThrow(()->new EntityNotFoundException("Student Profile not found"));
        Assessment assessment=Assessment.builder()
                .student(student)
                .answers(request.getAnswers())
                .build();
        Assessment saved = assessmentRepo.save(assessment);

        try {
            Map<String, Object> recResp = recommenderClient.assess(request.getAnswers());

            JsonNode scoresNode = objectMapper.valueToTree(recResp.get("scores"));
            JsonNode genProfileNode = objectMapper.valueToTree(recResp.get("generated_profile"));

            saved.setScores(scoresNode);
            saved.setGeneratedProfile(genProfileNode);
            saved = assessmentRepo.save(saved);

            RecommendationLog logEntry = RecommendationLog.builder()
                    .user(student.getUser() != null ? student.getUser() : null)
                    .inputPayload(request.getAnswers())
                    .recommendations(genProfileNode)
                    .explanation(objectMapper.valueToTree(recResp.get("explain")))
                    .createdAt(OffsetDateTime.now())
                    .build();
            recommendationLogRepo.save(logEntry);

        } catch (Exception ex) {
            RecommendationLog logEntry = RecommendationLog.builder()
                    .user(student.getUser() != null ? student.getUser() : null)
                    .inputPayload(request.getAnswers())
                    .recommendations(null)
                    .explanation(objectMapper.valueToTree(Map.of("error", ex.getMessage())))
                    .createdAt(OffsetDateTime.now())
                    .build();
            recommendationLogRepo.save(logEntry);
        }

        return toDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AssessmentResponse> getAssessmentsForStudent(Long studentId, Pageable pageable) {
        return assessmentRepo.findByStudentId(studentId,pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public AssessmentResponse getAssessment(Long id) {
        Assessment a = assessmentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assessment not found: " + id));
        return toDto(a);
    }

    private AssessmentResponse toDto(Assessment a) {
        return AssessmentResponse.builder()
                .id(a.getId())
                .studentId(a.getStudent().getId())
                .answers(a.getAnswers())
                .scores(a.getScores())
                .generatedProfile(a.getGeneratedProfile())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
