package com.example.investment.application.analytics;

import com.example.investment.domain.decision.Decision;
import com.example.investment.domain.decision.DecisionRepository;
import com.example.investment.domain.marketsnapshot.MarketSnapshot;
import com.example.investment.domain.marketsnapshot.MarketSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DecisionReturnRateExecutionService {

    private final DecisionRepository decisionRepository;
    private final MarketSnapshotRepository snapshotRepository;

    private static final int DEFAULT_WINDOW_DAYS = 7;
    @Transactional
    public int executeForUser(Long userId) {
        List<Decision> targets = decisionRepository
                .findByUser_IdAndReturnRateIsNullOrderByDecidedAtAsc(userId);

        int updated = 0;
        for (Decision d : targets) {
            boolean ok = computeAndSaveReturnRate(d, DEFAULT_WINDOW_DAYS);
            if (ok) updated++;
        }
        return updated;
    }
    private boolean computeAndSaveReturnRate(Decision d, int windowDays) {
        Long symbolId = d.getSymbol().getId();
        LocalDateTime decidedAt = d.getDecidedAt();
        LocalDateTime targetAt = decidedAt.plusDays(windowDays);

        // before: decidedAt 이하에서 가장 가까운 스냅샷
        Optional<MarketSnapshot> beforeOpt =
                snapshotRepository.findTopBySymbol_IdAndCapturedAtLessThanEqualOrderByCapturedAtDesc(symbolId, decidedAt);

        // after: targetAt 이상에서 가장 가까운 스냅샷
        Optional<MarketSnapshot> afterOpt =
                snapshotRepository.findTopBySymbol_IdAndCapturedAtGreaterThanEqualOrderByCapturedAtAsc(symbolId, targetAt);

        if (beforeOpt.isEmpty() || afterOpt.isEmpty()) {
            // 데이터 부족하면 이번 실행에서는 스킵
            return false;
        }

        BigDecimal beforePrice = beforeOpt.get().getPrice();
        BigDecimal afterPrice = afterOpt.get().getPrice();

        if (beforePrice == null || afterPrice == null) return false;
        if (beforePrice.compareTo(BigDecimal.ZERO) <= 0) return false;

        // returnRate = (after - before) / before
        BigDecimal rr = afterPrice
                .subtract(beforePrice)
                .divide(beforePrice, 6, RoundingMode.HALF_UP);

        d.recordReturnRate(rr); // 더티체킹으로 UPDATE
        return true;
    }
}
