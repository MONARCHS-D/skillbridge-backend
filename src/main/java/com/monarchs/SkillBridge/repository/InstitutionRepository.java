package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution,Long> {

}
