package com.example.investment.presentation.investmentlog.dto;

import com.example.investment.domain.investmentlog.DecisionType;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CreateInvestmentLogRequest {
    private String symbol;
    private DecisionType decisionType;
    private Long decisionPrice;
    private String reason;
    private LocalDateTime decidedAt;
}
