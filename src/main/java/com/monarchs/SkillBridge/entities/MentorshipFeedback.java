package com.monarchs.SkillBridge.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "mentorship_feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorshipFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorship_id",nullable = false)
    private Mentorship mentorship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_user_id")
    private User reviewerUser;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    private Short rating;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;
}
