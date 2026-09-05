package com.monarchs.SkillBridge.entities;

import com.monarchs.SkillBridge.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_institution_id")
    private Institution hostInstitution;

    @Column(nullable = false)
    private String title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "activity_type", nullable = false,length = 30)
    private ActivityType activityType;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    private String location;

    @Column(name = "registration_url",length = 2048 )
    private String registrationUrl;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

}
