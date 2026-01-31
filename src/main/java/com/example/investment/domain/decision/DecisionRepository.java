package com.example.investment.domain.decision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends JpaRepository<Decision,Long> {
    List<Decision> findByUser_IdAndSymbol_IdOrderByDecidedAtDesc(
            Long userId, Long symbolId
    );
    List<Decision> findByUser_IdOrderByDecidedAtDesc(Long userId);
    Optional<Decision> findByIdAndUser_Id(Long id, Long userId);
    @Query("""
        select
            e as emotion,
            count(d) as totalCount,
            sum(case when d.returnRate > 0 then 1 else 0 end) as winCount,
            sum(case when d.returnRate <= 0 or d.returnRate is null then 1 else 0 end) as lossCount,
            avg(d.returnRate) as avgReturnRate
        from Decision d
            join d.emotions e
        where d.user.id = :userId
        group by e
        order by count(d) desc
    """)
    List<EmotionAnalyticsProjection> analyzeEmotionStats(@Param("userId") Long userId);
}
