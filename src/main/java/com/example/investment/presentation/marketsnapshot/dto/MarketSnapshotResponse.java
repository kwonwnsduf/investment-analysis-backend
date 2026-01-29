package com.example.investment.presentation.marketsnapshot.dto;

import com.example.investment.domain.marketsnapshot.MarketSnapshot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketSnapshotResponse(Long id,
                                     Long symbolId,
                                     BigDecimal price,
                                     BigDecimal changeRate,
                                     BigDecimal per,
                                     BigDecimal pbr,
                                     BigDecimal roe,
                                     BigDecimal rsi,
                                     LocalDateTime capturedAt) {
    public static MarketSnapshotResponse from(MarketSnapshot s) {
        return new MarketSnapshotResponse(
                s.getId(),
                s.getSymbol().getId(),
                s.getPrice(),
                s.getChangeRate(),
                s.getPer(),
                s.getPbr(),
                s.getRoe(),
                s.getRsi(),
                s.getCapturedAt()
        );
    }

}
