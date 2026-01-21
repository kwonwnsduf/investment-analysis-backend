package com.example.investment.presentation.event;

import com.example.investment.application.event.EventService;
import com.example.investment.presentation.event.dto.EventCreateRequest;
import com.example.investment.presentation.event.dto.EventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody EventCreateRequest req){
        return ResponseEntity.ok(eventService.create(req));
    }
    @GetMapping
    public ResponseEntity<List<EventResponse>> list(@PathVariable String symbol){
        return ResponseEntity.ok(eventService.findBySymbol(symbol));
    }
}
