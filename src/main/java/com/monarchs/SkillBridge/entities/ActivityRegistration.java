package com.monarchs.SkillBridge.entities;

import com.monarchs.SkillBridge.enums.ActivityRegStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "activities_registration",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_activity_user",
                columnNames = {"activities_id","user_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activities_id",nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Builder.Default
    @Column(length = 30)
    private String status="REGISTERED";

    @CreationTimestamp
    @Column(name = "registered_at",updatable = false)
    private OffsetDateTime registeredAt;
}
