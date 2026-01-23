package com.example.investment.domain.companyevent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyEventRepository extends JpaRepository<CompanyEvent,Long> {
    List<CompanyEvent> findByUserIdAndSymbol_IdOrderByEventAtDesc(Long userId, Long symbolId);

    Optional<CompanyEvent> findByIdAndUserId(Long id, Long userId);
}
