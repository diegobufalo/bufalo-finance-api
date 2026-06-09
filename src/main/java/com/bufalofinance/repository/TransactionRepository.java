package com.bufalofinance.repository;

import com.bufalofinance.entity.Transaction;
import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

        Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

        List<Transaction> findByUserIdAndOccurredOnBetweenOrderByOccurredOnDesc(
                        UUID userId, LocalDate from, LocalDate to);

        List<Transaction> findByUserIdAndAccountIdAndOccurredOnBetweenOrderByOccurredOnDesc(
                        UUID userId, UUID accountId, LocalDate from, LocalDate to);

        List<Transaction> findByUserIdAndCategoryIdAndOccurredOnBetweenOrderByOccurredOnDesc(
                        UUID userId, UUID categoryId, LocalDate from, LocalDate to);

        List<Transaction> findByUserIdAndTypeAndOccurredOnBetweenOrderByOccurredOnDesc(
                        UUID userId, TransactionType type, LocalDate from, LocalDate to);

        // usado pelo BalanceService — @SQLRestriction já filtra deleted_at is null
        List<Transaction> findByAccountIdAndStatus(UUID accountId, TransactionStatus status);

        List<Transaction> findByTransferAccountIdAndStatus(UUID transferAccountId, TransactionStatus status);
}
