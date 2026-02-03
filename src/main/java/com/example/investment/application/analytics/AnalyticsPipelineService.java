package com.example.investment.application.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalyticsPipelineService {
    private final DecisionReturnRateExecutionService returnRateExecutor;
    private final AnalyticsExecutionService analyticsExecutor;
    @Transactional
    public AnalyticsPipelineResult execute(Long userId) {
        int updated = returnRateExecutor.executeForUser(userId);
        analyticsExecutor.executeAll(userId);

        return new AnalyticsPipelineResult(updated);
    }

    public record AnalyticsPipelineResult(int updatedReturnRates) {}
}

