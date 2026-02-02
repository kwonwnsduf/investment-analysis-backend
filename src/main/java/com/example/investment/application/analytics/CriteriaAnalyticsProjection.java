package com.example.investment.application.analytics;

import java.math.BigDecimal;

public interface CriteriaAnalyticsProjection {
    Long getCriteriaTagId();
    String getCriteriaName();
    Long getTotalCount();
    Long getWinCount();
    Long getLossCount();
    BigDecimal getAvgReturnRate();
}
