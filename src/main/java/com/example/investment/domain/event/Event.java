package com.example.investment.domain.event;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name="events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String symbol; //종목코드
    @Column(nullable=false)
    private String title;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column(nullable=false)
    private LocalDate eventDate;
    private Integer impactScore;
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Builder
    private Event(String symbol, String title, String description,
                  LocalDate eventDate, Integer impactScore, EventType type) {
        this.symbol = symbol;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.impactScore = impactScore;
        this.type = type;
    }

}
