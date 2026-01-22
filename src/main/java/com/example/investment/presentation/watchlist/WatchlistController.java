package com.example.investment.presentation.watchlist;

import com.example.investment.application.watchlistservice.WatchlistService;
import com.example.investment.presentation.symbol.dto.SymbolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    @PostMapping("/{symbolId}")
    public void add(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long symbolId
    ) {
        watchlistService.add(userId, symbolId);
    }
    @GetMapping
    public List<SymbolResponse> myWatchlist(
            @AuthenticationPrincipal Long userId
    ) {
        return watchlistService.myWatchlist(userId);
    }
    @DeleteMapping("/{symbolId}")
    public void remove(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long symbolId
    ) {
        watchlistService.remove(userId, symbolId);
    }
}
