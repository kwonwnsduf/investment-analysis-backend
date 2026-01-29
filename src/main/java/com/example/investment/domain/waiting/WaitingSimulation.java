package com.example.investment.domain.waiting;

import com.example.investment.domain.decision.Decision;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "waiting_simulations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"decision_id", "waitDays"})
)
public class WaitingSimulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "decision_id", nullable = false)
    private Decision decision;

    @Column(nullable = false)
    private int waitDays;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal basePrice;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal waitedPrice;

    // 수익률(%) : waited/base 대비 변화율
    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal returnRate;

    @Column(nullable = false)
    private LocalDateTime simulatedAt;
}
