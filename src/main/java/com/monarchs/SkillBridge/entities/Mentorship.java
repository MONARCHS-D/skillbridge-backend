package com.monarchs.SkillBridge.entities;

import com.monarchs.SkillBridge.enums.MentorshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "mentorship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mentorship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_user_id",nullable = false)
    private User mentorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_user_id",nullable = false)
    private User menteeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activities_id")
    private Activity activity;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false,length = 20)
    private MentorshipStatus status=MentorshipStatus.ACTIVE;
}
