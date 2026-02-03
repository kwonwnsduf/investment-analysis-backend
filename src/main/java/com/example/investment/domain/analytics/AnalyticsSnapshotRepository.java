package com.example.investment.domain.analytics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnalyticsSnapshotRepository extends JpaRepository<AnalyticsSnapshot,Long> {
    Optional<AnalyticsSnapshot> findTopByUserIdAndSnapshotTypeOrderByComputedAtDesc(Long userId, SnapshotType type);
}
