package com.example.investment.application.waiting;

import com.example.investment.domain.decision.Decision;
import com.example.investment.domain.decision.DecisionRepository;
import com.example.investment.domain.marketsnapshot.MarketSnapshot;
import com.example.investment.domain.marketsnapshot.MarketSnapshotRepository;
import com.example.investment.domain.waiting.WaitingSimulation;
import com.example.investment.domain.waiting.WaitingSimulationRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import com.example.investment.presentation.waiting.dto.WaitingSimulationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingSimulationService {
    private final DecisionRepository decisionRepository;
    private final MarketSnapshotRepository marketSnapshotRepository;
    private final WaitingSimulationRepository waitingSimulationRepository;
    public WaitingSimulationResponse simulate(Long decisionId, int waitDays) {
        if (waitDays <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }

        return waitingSimulationRepository.findByDecisionIdAndWaitDays(decisionId, waitDays)
                .map(this::toResponse)
                .orElseGet(() -> createNewSimulation(decisionId, waitDays));
    }
    private WaitingSimulationResponse createNewSimulation(Long decisionId, int waitDays) {
        Decision decision = decisionRepository.findById(decisionId)
                .orElseThrow(() -> new ApiException(ErrorCode.DECISION_NOT_FOUND));

        // 결정 시점 기준 "이전(포함)" 가장 가까운 가격
        MarketSnapshot baseSnap = marketSnapshotRepository
                .findTopBySymbol_IdAndCapturedAtLessThanEqualOrderByCapturedAtDesc(
                        decision.getSymbol().getId(), decision.getDecidedAt()
                )
                .orElseThrow(() -> new ApiException(
                        ErrorCode.SNAPSHOT_NOT_FOUND
                ));

        // waitDays 이후 목표 시점
        LocalDateTime targetAt = decision.getDecidedAt().plusDays(waitDays);

        // 목표 시점 기준 "이후(포함)" 가장 가까운 가격
        MarketSnapshot waitedSnap = marketSnapshotRepository
                .findTopBySymbol_IdAndCapturedAtGreaterThanEqualOrderByCapturedAtAsc(
                        decision.getSymbol().getId(), targetAt
                )
                .orElseThrow(() -> new ApiException(
                        ErrorCode.SNAPSHOT_NOT_FOUND

                ));

        BigDecimal basePrice = baseSnap.getPrice();
        BigDecimal waitedPrice = waitedSnap.getPrice();

        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }

        // returnRate(%) = (waited - base) / base * 100
        BigDecimal returnRate = waitedPrice
                .subtract(basePrice)
                .divide(basePrice, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        WaitingSimulation saved = waitingSimulationRepository.save(
                WaitingSimulation.builder()
                        .decision(decision)
                        .waitDays(waitDays)
                        .basePrice(basePrice)
                        .waitedPrice(waitedPrice)
                        .returnRate(returnRate)
                        .simulatedAt(LocalDateTime.now())
                        .build()
        );

        return toResponse(saved);
    }

    private WaitingSimulationResponse toResponse(WaitingSimulation sim) {
        return new WaitingSimulationResponse(
                sim.getId(),
                sim.getDecision().getId(),
                sim.getWaitDays(),
                sim.getBasePrice(),
                sim.getWaitedPrice(),
                sim.getReturnRate(),
                sim.getDecision().getDecidedAt(),
                sim.getSimulatedAt()
        );
    }
}
