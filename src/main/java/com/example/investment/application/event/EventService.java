package com.example.investment.application.event;

import com.example.investment.domain.event.Event;
import com.example.investment.domain.event.EventRepository;
import com.example.investment.presentation.event.dto.EventCreateRequest;
import com.example.investment.presentation.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    public Long create(EventCreateRequest req){
       Event event=Event.builder()
               .symbol(req.symbol())
               .title(req.title())
               .description(req.description())
               .eventDate(req.eventDate())
               .impactScore(req.impactScore())
               .type(req.type())
               .build();
       return eventRepository.save(event).getId();
    }
    @Transactional(readOnly = true)
    public List<EventResponse> findBySymbol(String symbol){
        return eventRepository.findBySymbolOrderByEventDateDesc(symbol).stream().map(EventResponse::from).toList();
    }
}
