package com.example.investment.domain.analytics;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "analytics_snapshots",
        indexes = {
                @Index(name = "idx_snapshot_user_type_time", columnList = "user_id, snapshot_type, computed_at")
        })
public class AnalyticsSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "snapshot_type", nullable = false, length = 30)
    private SnapshotType snapshotType;

    @Lob
    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "computed_at", nullable = false)
    private LocalDateTime computedAt;

    public static AnalyticsSnapshot of(Long userId, SnapshotType type, String payloadJson, LocalDateTime computedAt) {
        AnalyticsSnapshot s = new AnalyticsSnapshot();
        s.userId = userId;
        s.snapshotType = type;
        s.payloadJson = payloadJson;
        s.computedAt = computedAt == null ? LocalDateTime.now() : computedAt;
        return s;
    }
}
