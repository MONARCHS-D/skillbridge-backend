package com.monarchs.SkillBridge.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,unique = true,length = 512)
    private String website;

    @Column(name = "contact_email", nullable = false, unique = true)
    private String contactEmail;

    @Column(name = "contact_phone", nullable = false,unique = true,length = 32)
    private String contactPhone;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Posting> postings;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Activity> activities;
}
