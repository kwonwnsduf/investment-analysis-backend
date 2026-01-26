package com.example.investment.presentation.decision.dto;

import com.example.investment.domain.decision.EmotionTag;
import com.example.investment.domain.investmentlog.DecisionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class DecisionCreateRequest {
    @NotNull
    private Long symbolId;

    @NotNull
    private DecisionType type; // BUY / SELL / HOLD

    // 선택값
    private Set<EmotionTag> emotions;

    @Min(1)
    @Max(5)
    private Integer confidence;

    private String reason;
}
