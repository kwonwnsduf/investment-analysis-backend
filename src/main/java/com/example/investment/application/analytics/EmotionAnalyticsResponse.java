package com.example.investment.application.analytics;

import com.example.investment.domain.decision.EmotionTag;

import java.math.BigDecimal;

public record EmotionAnalyticsResponse(EmotionTag emotion,
                                       long totalCount,
                                       long winCount,
                                       long lossCount,
                                       BigDecimal avgReturnRate,
                                       BigDecimal winRate) {
}
