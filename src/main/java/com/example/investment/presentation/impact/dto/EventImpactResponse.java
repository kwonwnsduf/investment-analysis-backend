package com.example.investment.presentation.impact.dto;

import java.math.BigDecimal;

public record  EventImpactResponse (Long id,
                                    Long eventId,
                                    int windowDays,
                                    BigDecimal priceBefore,
                                    BigDecimal priceAfter,
                                    BigDecimal changeRate){
}
