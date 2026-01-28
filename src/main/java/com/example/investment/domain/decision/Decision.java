package com.example.investment.domain.decision;

import com.example.investment.domain.criteria.DecisionCriteria;
import com.example.investment.domain.investmentlog.DecisionType;
import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "decisions",
        indexes = {
                @Index(name = "idx_decision_user_symbol_decidedAt", columnList = "user_id, symbol_id, decided_at")
        })
public class Decision {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private DecisionType type;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "decision_emotions",
            joinColumns = @JoinColumn(name = "decision_id", nullable = false)
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "emotion", nullable = false, length = 30)
    private Set<EmotionTag> emotions = new HashSet<>();

    @Column(name = "confidence")
    private Integer confidence;

    @Column(name = "reason",columnDefinition = "TEXT")
    private String reason;

    @Column(name = "decided_at", nullable = false)
    private LocalDateTime decidedAt;

    @OneToMany(mappedBy = "decision", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DecisionCriteria> criteriaLinks = new HashSet<>();


    public static Decision create(
            User user,
            Symbol symbol,
            DecisionType type,
            Set<EmotionTag> emotions,
            Integer confidence,
            String reason,
            LocalDateTime decidedAt
    ) {
        Decision d = new Decision();
        d.user = requireNonNull(user, "user");
        d.symbol = requireNonNull(symbol, "symbol");
        d.type = requireNonNull(type, "type");

        // emotions는 null 들어올 수 있으니 방어
        d.emotions = (emotions == null) ? new HashSet<>() : new HashSet<>(emotions);

        // 선택 값
        d.confidence = confidence;
        d.reason = reason;

        // decidedAt은 null이면 now로
        d.decidedAt = (decidedAt == null) ? LocalDateTime.now() : decidedAt;

        // 간단한 도메인 규칙(원하면 강화 가능)
        d.validateConfidenceRange(d.confidence);

        return d;
    }
    public void changeType(DecisionType type) {
        this.type = requireNonNull(type, "type");
    }

    public void changeConfidence(Integer confidence) {
        validateConfidenceRange(confidence);
        this.confidence = confidence;
    }
    public void changeReason(String reason) {
        this.reason = reason;
    }
    public void replaceEmotions(Set<EmotionTag> newEmotions) {
        this.emotions.clear();
        if (newEmotions != null) {
            this.emotions.addAll(newEmotions);
        }
    }
    public void clearCriteriaLinks() {
        this.criteriaLinks.clear();
    }
    public void addCriteriaLink(DecisionCriteria link) {
        this.criteriaLinks.add(link);
    }
    private void validateConfidenceRange(Integer confidence) {
        if (confidence == null) return; // 선택값

        // 1~5 규칙(너가 원하면 1~10으로 바꿔도 됨)
        if (confidence < 1 || confidence > 5) {
            throw new IllegalArgumentException("confidence must be between 1 and 5");
        }
    }

    private static <T> T requireNonNull(T v, String name) {
        if (v == null) throw new IllegalArgumentException(name + " must not be null");
        return v;
    }

}
