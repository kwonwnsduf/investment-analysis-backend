package com.example.investment.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findBySymbolOrderByEventDateDesc(String symbol);
}
