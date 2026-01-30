package com.example.investment.presentation.impact;

import com.example.investment.application.impact.EventImpactService;
import com.example.investment.domain.impact.EventImpact;
import com.example.investment.domain.impact.EventImpactRepository;
import com.example.investment.presentation.impact.dto.EventImpactAnalyzeResponse;
import com.example.investment.presentation.impact.dto.EventImpactResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-impacts")
public class EventImpactController {
    private final EventImpactService impactService;
    private final EventImpactRepository impactRepository;
    @PostMapping("/{eventId}")
    public EventImpactAnalyzeResponse analyze(
            @PathVariable Long eventId,
            @RequestParam int windowDays
    ) {
        return impactService.analyze(eventId, windowDays);
    }
    @GetMapping("/{eventId}")
    public List<EventImpactResponse> list(@PathVariable Long eventId) {
        List<EventImpact> impacts = impactRepository.findByEvent_IdOrderByWindowDaysAsc(eventId);

        return impacts.stream()
                .map(i -> new EventImpactResponse(
                        i.getId(),
                        i.getEvent().getId(),
                        i.getWindowDays(),
                        i.getPriceBefore(),
                        i.getPriceAfter(),
                        i.getChangeRate()
                ))
                .toList();
    }
}
