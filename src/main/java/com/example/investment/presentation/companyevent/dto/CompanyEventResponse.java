package com.example.investment.presentation.companyevent.dto;

import com.example.investment.domain.companyevent.CompanyEvent;
import com.example.investment.domain.companyevent.EventType;

import java.time.LocalDateTime;

public record CompanyEventResponse(Long id,
                                   Long symbolId,
                                   String symbolCode,
                                   EventType type,
                                   LocalDateTime eventAt,
                                   String note,
                                   String sourceUrl) {
}
