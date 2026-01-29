package com.example.investment.presentation.marketsnapshot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketSnapshotRequest(BigDecimal price,
                                    BigDecimal changeRate,
                                    BigDecimal per,
                                    BigDecimal pbr,
                                    BigDecimal roe,
                                    BigDecimal rsi,
                                    LocalDateTime capturedAt) {
}
