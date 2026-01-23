package com.example.investment.application.companyevent;

import com.example.investment.domain.companyevent.CompanyEvent;
import com.example.investment.domain.companyevent.CompanyEventRepository;
import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.presentation.companyevent.dto.CompanyEventCreateRequest;
import com.example.investment.presentation.companyevent.dto.CompanyEventResponse;
import com.example.investment.presentation.companyevent.dto.CompanyEventUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyEventService {
    private final CompanyEventRepository companyEventRepository;
    private final SymbolRepository symbolRepository;
    public CompanyEventResponse create(Long userId, CompanyEventCreateRequest req) {
        Symbol symbol = symbolRepository.findById(req.symbolId())
                .orElseThrow(() -> new IllegalArgumentException("symbol not found"));

        CompanyEvent event = new CompanyEvent(
                userId, symbol, req.type(), req.eventAt(), req.note(), req.sourceUrl()
        );

        companyEventRepository.save(event);
        return toResponse(event);
    }
    @Transactional(readOnly = true)
    public List<CompanyEventResponse> timeline(Long userId, Long symbolId) {
        return companyEventRepository.findByUserIdAndSymbol_IdOrderByEventAtDesc(userId, symbolId)
                .stream().map(this::toResponse).toList();
    }
    public CompanyEventResponse update(Long userId, Long eventId, CompanyEventUpdateRequest req) {
        CompanyEvent event = companyEventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalArgumentException("event not found"));

        event.update(req.type(), req.eventAt(), req.note(), req.sourceUrl());
        return toResponse(event);
    }

    public void delete(Long userId, Long eventId) {
        CompanyEvent event = companyEventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new IllegalArgumentException("event not found"));
        companyEventRepository.delete(event);
    }


    private CompanyEventResponse toResponse(CompanyEvent e) {
        return new CompanyEventResponse(
                e.getId(),
                e.getSymbol().getId(),
                e.getSymbol().getCode(),   // code 필드명이 다르면 맞춰줘
                e.getType(),
                e.getEventAt(),
                e.getNote(),
                e.getSourceUrl()
        );
    }
}
