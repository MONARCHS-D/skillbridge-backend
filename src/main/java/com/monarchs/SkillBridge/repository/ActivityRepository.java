package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    Page<Activity> findByCompanyId(Long companyId,Pageable pageable);

    Page<Activity> findByHostInstitutionId(Long hostInstitutionId, Pageable pageable);

}
