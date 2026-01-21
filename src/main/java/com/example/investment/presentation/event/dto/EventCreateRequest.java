package com.example.investment.presentation.event.dto;

import com.example.investment.domain.event.EventType;

import java.time.LocalDate;

public record EventCreateRequest(String symbol,
                                 String title,
                                 String description,
                                 LocalDate eventDate,
                                 Integer impactScore,
                                 EventType type) {
}
