package com.example.investment.domain.criteria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DecisionCriteriaRepository extends JpaRepository<DecisionCriteria,Long> {
    List<DecisionCriteria> findByDecisionId(Long decisionId);
    void deleteByDecisionId(Long decisionId);
    boolean existsByDecisionIdAndCriteriaTagId(Long decisionId, Long criteriaTagId);
}
