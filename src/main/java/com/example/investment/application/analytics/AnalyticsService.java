package com.example.investment.application.analytics;

import com.example.investment.domain.decision.DecisionRepository;
import com.example.investment.domain.decision.EmotionAnalyticsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {
    private final DecisionRepository decisionRepository;
    public List<EmotionAnalyticsResponse> analyzeByEmotion(Long userId) {
        List<EmotionAnalyticsProjection> rows = decisionRepository.analyzeEmotionStats(userId);

        return rows.stream()
                .map(p -> new EmotionAnalyticsResponse(
                        p.getEmotion(),
                        safeLong(p.getTotalCount()),
                        safeLong(p.getWinCount()),
                        safeLong(p.getLossCount()),
                        safeBigDecimal(p.getAvgReturnRate()),
                        calcWinRate(p.getWinCount(), p.getTotalCount())
                ))
                .toList();
    }
    public List<CriteriaAnalyticsResponse> analyzeCriteria(Long userId) {
        List<CriteriaAnalyticsProjection> rows = decisionRepository.analyzeCriteriaStats(userId);

        return rows.stream()
                .map(p -> {
                    long total = safeLong(p.getTotalCount());
                    long win = safeLong(p.getWinCount());
                    long loss = safeLong(p.getLossCount());
                    BigDecimal avg = safeBigDecimal(p.getAvgReturnRate());

                    return new CriteriaAnalyticsResponse(
                            p.getCriteriaTagId(),
                            p.getCriteriaName(),
                            total,
                            win,
                            loss,
                            avg,
                            calcWinRate(win, total)
                    );
                })
                .toList();
    }
    private long safeLong(Long v) {
        return (v == null) ? 0L : v;
    }

    private BigDecimal safeBigDecimal(BigDecimal v) {
        return (v == null) ? BigDecimal.ZERO : v;
    }

    private BigDecimal calcWinRate(Long winCount, Long totalCount) {
        long win = safeLong(winCount);
        long total = safeLong(totalCount);
        if (total == 0) return BigDecimal.ZERO;

        // 승률: 0.0000 ~ 1.0000
        return BigDecimal.valueOf(win)
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP);
    }

}
