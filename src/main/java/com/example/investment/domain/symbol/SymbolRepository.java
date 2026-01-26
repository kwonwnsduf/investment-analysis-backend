package com.example.investment.domain.symbol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SymbolRepository extends JpaRepository<Symbol,Long> {
    Optional<Symbol> findByCode(String code);

    boolean existsByCode(String code);
}
