package com.example.investment.application.marketsnapshot;

import com.example.investment.domain.marketsnapshot.MarketSnapshot;
import com.example.investment.domain.marketsnapshot.MarketSnapshotRepository;
import com.example.investment.domain.symbol.Symbol;
import com.example.investment.domain.symbol.SymbolRepository;
import com.example.investment.global.exception.ApiException;
import com.example.investment.global.exception.ErrorCode;
import com.example.investment.presentation.marketsnapshot.dto.MarketSnapshotRequest;
import com.example.investment.presentation.marketsnapshot.dto.MarketSnapshotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MarketSnapshotService {

    private final MarketSnapshotRepository snapshotRepository;
    private final SymbolRepository symbolRepository;

    public MarketSnapshotResponse create(Long symbolId, MarketSnapshotRequest req) {
        if (req == null) {
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }
        if (req.price() == null || req.capturedAt() == null) {
            // price, capturedAt은 필수로 보자(프로젝트에선 이 정도는 강제하는 게 좋아)
            throw new ApiException(ErrorCode.INVALID_REQUEST);
        }
        Symbol symbol = symbolRepository.findById(symbolId)
                .orElseThrow(() -> new ApiException(ErrorCode.SYMBOL_NOT_FOUND));

        MarketSnapshot snapshot = MarketSnapshot.of(symbol, req);
        MarketSnapshot saved= snapshotRepository.save(snapshot);
        return MarketSnapshotResponse.from(saved);


    }
    @Transactional(readOnly = true)
    public MarketSnapshotResponse getLatest(Long symbolId) {
        MarketSnapshot snapshot = snapshotRepository.findTopBySymbol_IdOrderByCapturedAtDesc(symbolId)
                .orElseThrow(() -> new ApiException(ErrorCode.SNAPSHOT_NOT_FOUND));
        return MarketSnapshotResponse.from(snapshot);
    }
}
