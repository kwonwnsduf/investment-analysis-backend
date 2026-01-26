package com.example.investment.domain.symbol;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "symbols",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Symbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=30)
    private String code;   // AAPL, TSLA

    @Column(nullable = false,length=100)
    private String name;   // Apple Inc.

    private String market; // NASDAQ, NYSE

    public static Symbol create(String code, String name, String market) {
        Symbol s = new Symbol();
        s.code = code;
        s.name = name;
        s.market = market;
        return s;
    }
}
