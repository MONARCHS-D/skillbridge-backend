package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.ActivityRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration,Long> {

    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    Page<ActivityRegistration> findByActivityId(Long activityId, Pageable pageable);

    Page<ActivityRegistration> findByUserId(Long userId,Pageable pageable);
}
