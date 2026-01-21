package com.example.investment.presentation.investmentlog;

import com.example.investment.application.investmentlog.InvestmentLogService;
import com.example.investment.presentation.investmentlog.dto.CreateInvestmentLogRequest;
import com.example.investment.presentation.investmentlog.dto.InvestmentLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/investments")
public class InvestmentLogController {
    private final InvestmentLogService service;
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateInvestmentLogRequest req){
        return ResponseEntity.ok(service.create(req));
    }
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentLogResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }
}
