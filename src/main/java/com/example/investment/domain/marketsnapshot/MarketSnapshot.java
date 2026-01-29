package com.example.investment.domain.marketsnapshot;

import com.example.investment.domain.symbol.Symbol;
import com.example.investment.presentation.marketsnapshot.dto.MarketSnapshotRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "market_snapshots",
        indexes = {
                @Index(name = "idx_snapshot_symbol_time", columnList = "symbol_id, captured_at")
        })
public class MarketSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "symbol_id", nullable = false)
    private Symbol symbol;

    // 가격 정보
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
    @Column(precision = 10, scale = 4)
    private BigDecimal changeRate;   // 등락률

    // 밸류/지표
    @Column(precision = 10, scale = 4)
    private BigDecimal per;
    @Column(precision = 10, scale = 4)
    private BigDecimal pbr;
    @Column(precision = 10, scale = 4)
    private BigDecimal roe;

    // 기술적 지표
    @Column(precision = 10, scale = 4)
    private BigDecimal rsi;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt;

    public static MarketSnapshot of(Symbol symbol, MarketSnapshotRequest req) {
        return MarketSnapshot.builder()
                .symbol(symbol)
                .price(req.price())
                .changeRate(req.changeRate())
                .per(req.per())
                .pbr(req.pbr())
                .roe(req.roe())
                .rsi(req.rsi())
                .capturedAt(req.capturedAt())
                .build();
    }
}
