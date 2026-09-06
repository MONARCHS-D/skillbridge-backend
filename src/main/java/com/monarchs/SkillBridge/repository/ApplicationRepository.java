package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

    boolean existsByPostingIdAndStudentId(Long postingId, Long studentId);

    Page<Application> findByStudentId(Long studentId, Pageable pageable);

    Page<Application> findByPostingCompanyId(Long postingCompanyId,Pageable pageable);
}
