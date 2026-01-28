package com.example.investment.domain.criteria;

import com.example.investment.domain.decision.Decision;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "decision_criteria",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_decision_criteria",
                columnNames = {"decision_id", "criteria_tag_id"}
        ),
        indexes = {
                @Index(name = "idx_decision_criteria_decision", columnList = "decision_id"),
                @Index(name = "idx_decision_criteria_tag", columnList = "criteria_tag_id")
        }
)
public class DecisionCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "decision_id", nullable = false)
    private Decision decision;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criteria_tag_id", nullable = false)
    private CriteriaTag criteriaTag;
    public static DecisionCriteria link(Decision decision, CriteriaTag tag) {
        DecisionCriteria dc = new DecisionCriteria();
        dc.decision = requireNonNull(decision, "decision");
        dc.criteriaTag = requireNonNull(tag, "criteriaTag");
        return dc;}
    private static <T> T requireNonNull(T v, String name) {
        if (v == null) throw new IllegalArgumentException(name + " must not be null");
        return v;
    }
}
