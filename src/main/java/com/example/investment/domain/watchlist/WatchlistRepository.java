package com.example.investment.domain.watchlist;

import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist,Long> {

    boolean existsByUserAndSymbol(User user, Symbol symbol);

    List<Watchlist> findAllByUser(User user);

    Optional<Watchlist> findByUserAndSymbol(User user, Symbol symbol);
}
