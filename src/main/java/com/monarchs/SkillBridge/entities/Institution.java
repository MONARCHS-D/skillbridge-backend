package com.monarchs.SkillBridge.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "institutions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private User user;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String address;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "institution",cascade = CascadeType.ALL)
    private List<Academician> academicians;

    @OneToMany(mappedBy = "institution",cascade = CascadeType.ALL)
    private List<Student> students;
}
