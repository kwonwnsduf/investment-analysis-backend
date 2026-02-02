package com.example.investment.application.analytics;

import java.math.BigDecimal;

public record CriteriaAnalyticsResponse(Long criteriaTagId,
                                        String criteriaName,
                                        Long totalCount,
                                        Long winCount,
                                        Long lossCount,
                                        BigDecimal avgReturnRate,
                                        BigDecimal winRate) {
}
