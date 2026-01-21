package com.example.investment.domain.investmentlog;
import com.example.investment.domain.investmentlog.DecisionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "investment_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol; // ex) AAPL, 삼성전자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionType decisionType;

    @Column(nullable = false)
    private Long decisionPrice;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private LocalDateTime decidedAt;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    private InvestmentLog(
            String symbol,
            DecisionType decisionType,
            Long decisionPrice,
            String reason,
            LocalDateTime decidedAt
    ) {
        this.symbol = symbol;
        this.decisionType = decisionType;
        this.decisionPrice = decisionPrice;
        this.reason = reason;
        this.decidedAt = decidedAt;
    }
}
