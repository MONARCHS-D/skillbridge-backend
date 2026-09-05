package com.monarchs.SkillBridge.entities;

import com.monarchs.SkillBridge.enums.UserRole;
import com.monarchs.SkillBridge.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash",nullable = false)
    private String password;

    @Column(nullable = false,unique = true,length = 32)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 30)
    private UserRole role;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 20)
    private UserStatus status=UserStatus.PENDING_APPROVAL;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Student student;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Institution institution;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Academician academician;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Company company;
}
