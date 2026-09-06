package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Assessment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment,Long> {

    Page<Assessment> findByStudentId(Long studentId, Pageable pageable);
}
