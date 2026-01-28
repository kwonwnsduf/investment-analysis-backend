package com.example.investment.domain.criteria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CriteriaTagRepository extends JpaRepository<CriteriaTag,Long> {
    Optional<CriteriaTag> findByName(String name);
    boolean existsByName(String name);
}
