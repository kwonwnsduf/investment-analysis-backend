package com.example.investment.presentation.analytics;

import com.example.investment.application.analytics.AnalyticsPipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsPipelineController {
    private final AnalyticsPipelineService pipelineService;

    @PostMapping("/execute")
    public AnalyticsPipelineService.AnalyticsPipelineResult execute(@AuthenticationPrincipal Long userId) {
        return pipelineService.execute(userId);
    }
}
