package com.example.investment.application.impact;

import com.example.investment.domain.companyevent.CompanyEvent;
import com.example.investment.domain.companyevent.CompanyEventRepository;
import com.example.investment.domain.impact.EventImpact;
import com.example.investment.domain.impact.EventImpactRepository;
import com.example.investment.domain.marketsnapshot.MarketSnapshot;
import com.example.investment.domain.marketsnapshot.MarketSnapshotRepository;
import com.example.investment.presentation.impact.dto.EventImpactAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EventImpactService {
    private final CompanyEventRepository eventRepository;
    private final MarketSnapshotRepository snapshotRepository;
    private final EventImpactRepository impactRepository;
    public EventImpactAnalyzeResponse analyze(Long eventId,int windowDays){
        if (windowDays <= 0) {
            throw new IllegalArgumentException("windowDays must be positive");
        }
        CompanyEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("event not found: " + eventId));

        LocalDateTime eventAt = event.getEventAt();
        MarketSnapshot before = snapshotRepository
                .findTopBySymbol_IdAndCapturedAtLessThanEqualOrderByCapturedAtDesc(event.getSymbol().getId(), eventAt)
                .orElseThrow(() -> new IllegalStateException("before snapshot missing (symbolId=" +
                        event.getSymbol().getId() + ", eventAt=" + eventAt + ")"));
        LocalDateTime target = eventAt.plusDays(windowDays);
        MarketSnapshot after = snapshotRepository
                .findTopBySymbol_IdAndCapturedAtGreaterThanEqualOrderByCapturedAtAsc(event.getSymbol().getId(), target)
                .orElseThrow(() -> new IllegalStateException("after snapshot missing (symbolId=" +
                        event.getSymbol().getId() + ", target=" + target + ")"));
        BigDecimal priceBefore = before.getPrice();
        BigDecimal priceAfter = after.getPrice();
        if (priceBefore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("invalid priceBefore: " + priceBefore);
        }
        BigDecimal changeRate = priceAfter
                .subtract(priceBefore)
                .divide(priceBefore, 8, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(4, RoundingMode.HALF_UP);
        EventImpact impact = impactRepository.findByEvent_IdAndWindowDays(eventId, windowDays)
                .map(existing -> EventImpact.builder()
                        .id(existing.getId())
                        .event(existing.getEvent())
                        .windowDays(existing.getWindowDays())
                        .priceBefore(priceBefore)
                        .priceAfter(priceAfter)
                        .changeRate(changeRate)
                        .build()
                )
                .orElseGet(() -> EventImpact.builder()
                        .event(event)
                        .windowDays(windowDays)
                        .priceBefore(priceBefore)
                        .priceAfter(priceAfter)
                        .changeRate(changeRate)
                        .build()
                );

        EventImpact saved = impactRepository.save(impact);
        return new EventImpactAnalyzeResponse(
                saved.getId(),
                event.getId(),
                windowDays,
                eventAt,
                before.getCapturedAt(),
                after.getCapturedAt(),
                priceBefore,
                priceAfter,
                changeRate
        );


    }
}
