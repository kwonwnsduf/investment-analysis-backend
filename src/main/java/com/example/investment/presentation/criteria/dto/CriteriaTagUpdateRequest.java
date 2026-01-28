package com.example.investment.presentation.criteria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaTagUpdateRequest {
    @NotBlank
    private String name;
}
