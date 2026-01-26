package com.example.investment.presentation.symbol.dto;

import jakarta.validation.constraints.NotBlank;

public record SymbolCreateRequest(@NotBlank String code,
                                  @NotBlank String name,
                                  String market) {
}
