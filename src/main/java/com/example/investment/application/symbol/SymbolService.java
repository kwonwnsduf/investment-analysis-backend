package com.example.investment.application.symbol;

import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.presentation.symbol.dto.SymbolCreateRequest;
import com.example.investment.presentation.symbol.dto.SymbolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SymbolService {
    private final SymbolRepository symbolRepository;
    public SymbolResponse create(SymbolCreateRequest req) {
        if (symbolRepository.existsByCode(req.code())) {
            throw new IllegalArgumentException("Symbol code already exists: " + req.code());
        }

        Symbol symbol = Symbol.create(
                req.code(),
                req.name(),
                req.market()
        );

        Symbol saved = symbolRepository.save(symbol);
        return SymbolResponse.from(saved);
    }
    @Transactional(readOnly = true)
    public List<SymbolResponse> findAll() {
        return symbolRepository.findAll()
                .stream()
                .map(SymbolResponse::from)
                .toList();
    }
    @Transactional(readOnly = true)
    public SymbolResponse findOne(Long symbolId) {
        Symbol symbol = symbolRepository.findById(symbolId)
                .orElseThrow(() -> new IllegalArgumentException("Symbol not found: " + symbolId));
        return SymbolResponse.from(symbol);
    }
}
