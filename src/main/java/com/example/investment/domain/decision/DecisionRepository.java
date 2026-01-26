package com.example.investment.domain.decision;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends JpaRepository<Decision,Long> {
    List<Decision> findByUser_IdAndSymbol_IdOrderByDecidedAtDesc(
            Long userId, Long symbolId
    );
    List<Decision> findByUser_IdOrderByDecidedAtDesc(Long userId);
    Optional<Decision> findByIdAndUser_Id(Long id, Long userId);

}
