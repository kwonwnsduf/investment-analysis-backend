package com.example.investment.presentation.decision.dto;

import com.example.investment.domain.decision.EmotionTag;
import com.example.investment.domain.investmentlog.DecisionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class DecisionUpdateRequest {
    private DecisionType type;
    private Set<EmotionTag> emotions;
    @Min(1)
    @Max(5)
    private Integer confidence;
    private String reason;
}
