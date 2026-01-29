package com.example.investment.domain.waiting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WaitingSimulationRepository extends JpaRepository<WaitingSimulation,Long> {
    Optional<WaitingSimulation> findByDecisionIdAndWaitDays(Long decisionId, int waitDays);

}
