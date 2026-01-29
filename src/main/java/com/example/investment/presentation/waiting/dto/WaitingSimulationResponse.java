package com.example.investment.presentation.waiting.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WaitingSimulationResponse(Long id,
                                        Long decisionId,
                                        int waitDays,
                                        BigDecimal basePrice,
                                        BigDecimal waitedPrice,
                                        BigDecimal returnRate,
                                        LocalDateTime decidedAt,
                                        LocalDateTime simulatedAt) {
}
