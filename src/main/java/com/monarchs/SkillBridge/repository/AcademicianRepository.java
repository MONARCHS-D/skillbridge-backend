package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Academician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicianRepository extends JpaRepository<Academician,Long> {
}
