package com.example.investment.presentation.symbol.dto;

import com.example.investment.domain.symbol.Symbol;

public record SymbolResponse(Long id,
                             String code,
                             String name,
                             String market) {
    public static SymbolResponse from(Symbol symbol) {
        return new SymbolResponse(
                symbol.getId(),
                symbol.getCode(),
                symbol.getName(),
                symbol.getMarket()
        );
    }
}
