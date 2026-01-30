package com.example.investment.presentation.impact.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventImpactAnalyzeResponse(Long impactId,
                                         Long eventId,
                                         int windowDays,

                                         LocalDateTime eventAt,
                                         LocalDateTime beforeCapturedAt,
                                         LocalDateTime afterCapturedAt,

                                         BigDecimal priceBefore,
                                         BigDecimal priceAfter,
                                         BigDecimal changeRate) {
}
