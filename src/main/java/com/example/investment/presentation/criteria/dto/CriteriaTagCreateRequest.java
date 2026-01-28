package com.example.investment.presentation.criteria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriteriaTagCreateRequest {
    @NotBlank
    private String name;
}
