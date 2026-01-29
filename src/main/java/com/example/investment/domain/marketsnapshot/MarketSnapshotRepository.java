package com.example.investment.domain.marketsnapshot;

import com.example.investment.domain.symbol.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarketSnapshotRepository extends JpaRepository<MarketSnapshot,Long> {
    Optional<MarketSnapshot> findTopBySymbol_IdOrderByCapturedAtDesc(Long symbolId);
    List<MarketSnapshot> findBySymbol_IdOrderByCapturedAtDesc(Long symbolId);
    Optional<MarketSnapshot> findTopBySymbol_IdAndCapturedAtLessThanEqualOrderByCapturedAtDesc(
            Long symbolId, LocalDateTime at
    );

    // 결정+waitDays 후 "이후(포함)" 가장 가까운 스냅샷
    Optional<MarketSnapshot> findTopBySymbol_IdAndCapturedAtGreaterThanEqualOrderByCapturedAtAsc(
            Long symbolId, LocalDateTime at
    );
}
