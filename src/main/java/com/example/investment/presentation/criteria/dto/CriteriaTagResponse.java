package com.example.investment.presentation.criteria.dto;

import com.example.investment.domain.criteria.CriteriaTag;
import lombok.Getter;

@Getter
public class CriteriaTagResponse {
    private Long id;
    private String name;
    public static CriteriaTagResponse from(CriteriaTag t) {
        CriteriaTagResponse res = new CriteriaTagResponse();
        res.id = t.getId();
        res.name = t.getName();
        return res;
    }
}
