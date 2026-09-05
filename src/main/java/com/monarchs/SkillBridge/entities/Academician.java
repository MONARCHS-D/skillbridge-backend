package com.monarchs.SkillBridge.entities;

import tools.jackson.databind.JsonNode;
import com.monarchs.SkillBridge.enums.Designation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "academicians")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Academician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id",nullable = false)
    private Institution institution;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 40)
    private Designation designation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "area_of_interest",columnDefinition = "jsonb")
    private JsonNode areaOfInterest;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
