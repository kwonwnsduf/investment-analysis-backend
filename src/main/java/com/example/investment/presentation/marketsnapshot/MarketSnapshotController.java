package com.example.investment.presentation.marketsnapshot;

import com.example.investment.application.marketsnapshot.MarketSnapshotService;
import com.example.investment.presentation.marketsnapshot.dto.MarketSnapshotRequest;
import com.example.investment.presentation.marketsnapshot.dto.MarketSnapshotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/snapshots")
public class MarketSnapshotController {
    private final MarketSnapshotService snapshotService;
    // 저장: /api/snapshots/{symbolId}
    @PostMapping("/{symbolId}")
    public MarketSnapshotResponse create(
            @PathVariable Long symbolId,
            @RequestBody MarketSnapshotRequest req
    ) {
        return snapshotService.create(symbolId, req);
    }

    // 최신 조회: /api/snapshots/{symbolId}/latest
    @GetMapping("/{symbolId}/latest")
    public MarketSnapshotResponse latest(@PathVariable Long symbolId) {
        return snapshotService.getLatest(symbolId);
    }
}
