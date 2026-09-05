package com.monarchs.SkillBridge.entities;

import tools.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "postings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id",nullable = false)
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    private String location;

    @Builder.Default
    @Column(name = "is_remote")
    private Boolean isRemote=false;

    private String stipend;

    @Column(name = "employment_type",nullable = false,length = 20)
    private String employmentType;

    @Transient
    private float[] embeddingVector;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "required_skills",columnDefinition = "jsonb")
    private JsonNode requiredSkills;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode metadata;

    private OffsetDateTime deadline;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
