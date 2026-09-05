package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
