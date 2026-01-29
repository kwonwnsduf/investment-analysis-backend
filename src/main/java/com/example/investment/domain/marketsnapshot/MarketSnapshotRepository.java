package com.example.investment.domain.marketsnapshot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketSnapshotRepository extends JpaRepository<MarketSnapshot,Long> {
    Optional<MarketSnapshot> findTopBySymbol_IdOrderByCapturedAtDesc(Long symbolId);
    List<MarketSnapshot> findBySymbol_IdOrderByCapturedAtDesc(Long symbolId);

}
