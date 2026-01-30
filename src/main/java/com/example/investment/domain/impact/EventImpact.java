package com.example.investment.domain.impact;

import com.example.investment.domain.companyevent.CompanyEvent;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="event_impacts", uniqueConstraints = {@UniqueConstraint(name= "uk_event_window", columnNames = {"event_id", "windowDays"})},
indexes = {@Index(name="idx_impact_event",columnList = "event_id")})
public class EventImpact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="event_id",nullable=false)
    private CompanyEvent event;
    @Column(nullable = false)
    private int windowDays;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal priceBefore;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal priceAfter;
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal changeRate;
}
