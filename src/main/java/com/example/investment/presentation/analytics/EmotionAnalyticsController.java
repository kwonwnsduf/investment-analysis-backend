package com.example.investment.presentation.analytics;

import com.example.investment.application.analytics.EmotionAnalyticsResponse;
import com.example.investment.application.analytics.EmotionAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class EmotionAnalyticsController {
    private final EmotionAnalyticsService emotionAnalyticsService;
    @GetMapping("/emotions")
    public List<EmotionAnalyticsResponse> emotions(@AuthenticationPrincipal Long userId){
        return emotionAnalyticsService.analyzeByEmotion(userId);
    }
}
