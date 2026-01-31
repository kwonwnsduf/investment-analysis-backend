package com.example.investment.domain.decision;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmotionAnalyticsProjection {
    EmotionTag getEmotion();
    Long getTotalCount();
    Long getWinCount();
    Long getLossCount();
    BigDecimal getAvgReturnRate();

}
