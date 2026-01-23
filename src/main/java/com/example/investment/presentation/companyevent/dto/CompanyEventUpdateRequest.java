package com.example.investment.presentation.companyevent.dto;

import com.example.investment.domain.companyevent.EventType;

import java.time.LocalDateTime;

public record CompanyEventUpdateRequest(EventType type,
                                        LocalDateTime eventAt,
                                        String note,
                                        String sourceUrl) {
}
