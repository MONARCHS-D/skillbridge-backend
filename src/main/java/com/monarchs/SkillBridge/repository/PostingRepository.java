package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
}
