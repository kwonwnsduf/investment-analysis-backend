package com.example.investment.presentation.event.dto;

import com.example.investment.domain.event.Event;
import com.example.investment.domain.event.EventType;

import java.time.LocalDate;

public record EventResponse(Long id,
                            String title,
                            LocalDate eventDate,
                            Integer impactScore,
                            EventType type) {
    public static EventResponse from(Event e){
        return new EventResponse(
                e.getId(),
                e.getTitle(),
                e.getEventDate(),
                e.getImpactScore(),
                e.getType()
        );
    }
}
