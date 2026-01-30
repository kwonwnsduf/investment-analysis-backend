package com.example.investment.domain.impact;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventImpactRepository extends JpaRepository<EventImpact, Long> {
    List<EventImpact> findByEvent_IdOrderByWindowDaysAsc(Long eventId);

    Optional<EventImpact> findByEvent_IdAndWindowDays(Long eventId, int windowDays);

    boolean existsByEvent_IdAndWindowDays(Long eventId, int windowDays);

}
