package com.bufalofinance.repository;

import com.bufalofinance.entity.RecurrenceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecurrenceRuleRepository extends JpaRepository<RecurrenceRule, UUID> {
    List<RecurrenceRule> findByUserId(UUID userId);

    Optional<RecurrenceRule> findByIdAndUserId(UUID id, UUID userId);

    List<RecurrenceRule> findByActiveTrue();
}
