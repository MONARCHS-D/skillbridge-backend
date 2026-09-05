package com.monarchs.SkillBridge.entities;

import tools.jackson.databind.JsonNode;
import com.monarchs.SkillBridge.enums.NotificationStatus;
import com.monarchs.SkillBridge.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 50,nullable = false)
    private NotificationType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    @Column(length = 50,nullable = false)
    private NotificationStatus status=NotificationStatus.PENDING;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;
}
