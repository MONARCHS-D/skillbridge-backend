package com.monarchs.SkillBridge.entities;

import com.monarchs.SkillBridge.enums.InternshipStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "internship_progress")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternshipProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id",nullable = false)
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_user_id")
    private Mentorship mentorship;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 30)
    private InternshipStatus status=InternshipStatus.STARTED;

    @Column(name = "report_url",length = 2048)
    private String reportUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "reported_at",updatable = false)
    private OffsetDateTime reportedAt;
}
