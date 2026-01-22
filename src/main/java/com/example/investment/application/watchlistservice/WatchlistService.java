package com.example.investment.application.watchlistservice;

import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.domain.user.User;
import com.example.investment.domain.user.UserRepository;
import com.example.investment.domain.watchlist.Watchlist;
import com.example.investment.domain.watchlist.WatchlistRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import com.example.investment.presentation.symbol.dto.SymbolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final SymbolRepository symbolRepository;
    public void add(Long userId, Long symbolId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Symbol symbol = symbolRepository.findById(symbolId)
                .orElseThrow(() -> new ApiException(ErrorCode.SYMBOL_NOT_FOUND));
        if (watchlistRepository.existsByUserAndSymbol(user, symbol)) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);}
            Watchlist watchlist = Watchlist.builder()
                    .user(user)
                    .symbol(symbol)
                    .build();

            watchlistRepository.save(watchlist);
        }
    public List<SymbolResponse> myWatchlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return watchlistRepository.findAllByUser(user).stream()
                .map(w -> SymbolResponse.from(w.getSymbol()))
                .toList();
    }
    public void remove(Long userId, Long symbolId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Symbol symbol = symbolRepository.findById(symbolId)
                .orElseThrow(() -> new ApiException(ErrorCode.SYMBOL_NOT_FOUND));

        Watchlist watchlist = watchlistRepository.findByUserAndSymbol(user, symbol)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_REQUEST));

        watchlistRepository.delete(watchlist);
    }

    }

