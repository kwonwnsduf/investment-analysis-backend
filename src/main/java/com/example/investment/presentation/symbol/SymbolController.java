package com.example.investment.presentation.symbol;

import com.example.investment.application.symbol.SymbolService;
import com.example.investment.presentation.symbol.dto.SymbolCreateRequest;
import com.example.investment.presentation.symbol.dto.SymbolResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/symbols")
public class SymbolController {
    private final SymbolService symbolService;
    @PostMapping
    public SymbolResponse create(@RequestBody @Valid SymbolCreateRequest req) {
        return symbolService.create(req);
    }
    @GetMapping
    public List<SymbolResponse> list() {
        return symbolService.findAll();
    }
    @GetMapping("/{symbolId}")
    public SymbolResponse get(@PathVariable Long symbolId) {
        return symbolService.findOne(symbolId);
    }
}
