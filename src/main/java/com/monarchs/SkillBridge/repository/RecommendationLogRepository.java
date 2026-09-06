package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.RecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog,Long> {
}
