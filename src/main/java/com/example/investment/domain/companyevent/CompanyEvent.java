package com.example.investment.domain.companyevent;

import com.example.investment.domain.symbol.Symbol;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes={@Index(name="idx_event_usr_symbol_at",columnList  ="user_id,symbol_id, event_at")})
public class CompanyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="symbol_id", nullable = false)
    private Symbol symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;
    @Column(nullable = false)
    private LocalDateTime eventAt;

    @Column(columnDefinition = "TEXT")
    private String note;

    private String sourceUrl;


    public CompanyEvent(Long userId, Symbol symbol, EventType type,
                        LocalDateTime eventAt, String note, String sourceUrl) {
        this.userId = userId;
        this.symbol = symbol;
        this.type = type;
        this.eventAt = eventAt;
        this.note = note;
        this.sourceUrl = sourceUrl;
    }
    public void update(EventType type, LocalDateTime eventAt, String note, String sourceUrl) {
        this.type = type;
        this.eventAt = eventAt;
        this.note = note;
        this.sourceUrl = sourceUrl;
    }
}
