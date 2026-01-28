package com.example.investment.presentation.decision.dto;

import com.example.investment.domain.criteria.DecisionCriteria;
import com.example.investment.domain.decision.Decision;
import com.example.investment.domain.decision.EmotionTag;
import com.example.investment.domain.investmentlog.DecisionType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class DecisionResponse {
    private Long id;
    private Long symbolId;
    private String symbolTicker;
    private String symbolName;

    private DecisionType type;
    private Set<EmotionTag> emotions;
    private Set<CriteriaTagDto> criteriaTags;

    private Integer confidence;
    private String reason;
    private LocalDateTime decidedAt;

    @Getter
    public static class CriteriaTagDto {
        private Long id;
        private String name;

        public static CriteriaTagDto of(Long id, String name) {
            CriteriaTagDto dto = new CriteriaTagDto();
            dto.id = id;
            dto.name = name;
            return dto;
        }
    }
    public static DecisionResponse from(Decision d) {
        DecisionResponse res = new DecisionResponse();
        res.id = d.getId();
        res.symbolId = d.getSymbol().getId();
        res.symbolTicker = d.getSymbol().getCode();
        res.symbolName = d.getSymbol().getName();
        res.type = d.getType();
        res.emotions = d.getEmotions();
        res.criteriaTags = d.getCriteriaLinks().stream()
                .map(DecisionCriteria::getCriteriaTag)
                .map(t -> CriteriaTagDto.of(t.getId(), t.getName()))
                .collect(Collectors.toSet());
        res.confidence = d.getConfidence();
        res.reason = d.getReason();
        res.decidedAt = d.getDecidedAt();
        return res;
    }
}
