package com.example.investment.presentation.analytics;

import com.example.investment.application.analytics.AnalyticsExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsExecutionController {

    private final AnalyticsExecutionService executionService;

    @PostMapping("/execute")
    public void executeAll(@AuthenticationPrincipal Long userId) {
        executionService.executeAll(userId);
    }
    @PostMapping("/execute/emotions")
    public void executeEmotions(@AuthenticationPrincipal Long userId) {
        executionService.executeEmotion(userId);
    }

    @PostMapping("/execute/criteria")
    public void executeCriteria(@AuthenticationPrincipal Long userId) {
        executionService.executeCriteria(userId);
    }
}
