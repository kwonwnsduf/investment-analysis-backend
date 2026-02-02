package com.example.investment.presentation.analytics;

import com.example.investment.application.analytics.CriteriaAnalyticsResponse;
import com.example.investment.application.analytics.EmotionAnalyticsResponse;
import com.example.investment.application.analytics.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    @GetMapping("/emotions")
    public List<EmotionAnalyticsResponse> emotions(@AuthenticationPrincipal Long userId){
        return analyticsService.analyzeByEmotion(userId);
    }
    @GetMapping("/criteria")
    public List<CriteriaAnalyticsResponse> criteriaStats(
            @AuthenticationPrincipal Long userId
    ) {
        return analyticsService.analyzeCriteria(userId);
    }
}
