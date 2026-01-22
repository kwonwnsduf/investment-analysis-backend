package com.example.investment.domain.symbol;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "symbols")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Symbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;   // AAPL, TSLA

    @Column(nullable = false)
    private String name;   // Apple Inc.

    private String market; // NASDAQ, NYSE
}
