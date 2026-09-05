package com.monarchs.SkillBridge.entities;

import tools.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",nullable = false)
    private Student student;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode answers;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode scores;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "generated_profile", columnDefinition = "jsonb")
    private JsonNode generatedProfile;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;
}
