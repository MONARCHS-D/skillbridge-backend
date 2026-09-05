package com.monarchs.SkillBridge.repository;

import com.monarchs.SkillBridge.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(@NotBlank(message = "Write Your Email") @Email String email);

    Optional<User> findByEmail(String email);
}
