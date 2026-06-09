package com.bufalofinance.repository;

import com.bufalofinance.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, String> {
    Optional<InviteCode> findByCodeAndUsedByIsNull(String code);
}
