package com.monarchs.SkillBridge.entities;

import tools.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "recommendation_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_payload",columnDefinition = "jsonb")
    private JsonNode inputPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode recommendations;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode explanation;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;
}
