package com.example.investment.domain.decision;

import com.example.investment.application.analytics.CriteriaAnalyticsProjection;
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
    List<Decision> findByUser_IdAndReturnRateIsNullOrderByDecidedAtAsc(Long userId);
    List<Decision> findByUser_IdAndSymbol_IdAndReturnRateIsNullOrderByDecidedAtAsc(Long userId, Long symbolId);
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

    @Query("""
        select
            ct.id as criteriaTagId,
            ct.name as criteriaName,
            count(d.id) as totalCount,
            sum(case when d.returnRate > 0 then 1 else 0 end) as winCount,
            sum(case when d.returnRate < 0 then 1 else 0 end) as lossCount,
            avg(d.returnRate) as avgReturnRate
        from Decision d
        join d.criteriaLinks dc
        join dc.criteriaTag ct
        where d.user.id = :userId
          and d.returnRate is not null
        group by ct.id, ct.name
        order by totalCount desc
    """)
    List<CriteriaAnalyticsProjection> analyzeCriteriaStats(@Param("userId") Long userId);
}
