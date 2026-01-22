package com.example.investment.domain.watchlist;

import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "watchlists",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "symbol_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Watchlist {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "symbol_id")
        private Symbol symbol;

        private LocalDateTime createdAt;
        @PrePersist
        void onCreate() {
                this.createdAt = LocalDateTime.now();
        }

        @Builder
        private Watchlist(User user, Symbol symbol) {
                this.user = user;
                this.symbol = symbol;
        }
}
