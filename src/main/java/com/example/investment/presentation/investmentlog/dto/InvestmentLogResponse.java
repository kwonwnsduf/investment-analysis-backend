package com.example.investment.presentation.investmentlog.dto;

import com.example.investment.domain.investmentlog.DecisionType;
import com.example.investment.domain.investmentlog.InvestmentLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InvestmentLogResponse {
    private Long id;
    private String symbol;
    private DecisionType decisionType;
    private Long decisionPrice;
    private String reason;
    private LocalDateTime decidedAt;
    public static InvestmentLogResponse from(InvestmentLog log){
            return  new InvestmentLogResponse(log.getId(), log.getSymbol(), log.getDecisionType()
            , log.getDecisionPrice(), log.getReason(), log.getDecidedAt());
    }
}
