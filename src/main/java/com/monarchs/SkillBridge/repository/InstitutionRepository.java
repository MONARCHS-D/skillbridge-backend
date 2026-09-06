package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Company;
import com.monarchs.SkillBridge.entities.Institution;
import com.monarchs.SkillBridge.projection.InstitutionDropdownView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution,Long> {

    @Query(value = "select id,name from institutions order by name asc",nativeQuery = true)
    List<InstitutionDropdownView> findAllInstituteAsc();

    Optional<Institution> findByUserId(Long userId);
}
