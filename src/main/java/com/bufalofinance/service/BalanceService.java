package com.bufalofinance.service;

import com.bufalofinance.dto.response.AccountBalanceResponse;
import com.bufalofinance.dto.response.SummaryResponse;
import com.bufalofinance.entity.Account;
import com.bufalofinance.entity.Transaction;
import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.entity.enums.TransactionType;
import com.bufalofinance.repository.AccountRepository;
import com.bufalofinance.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {

        private final AccountRepository accountRepository;
        private final TransactionRepository transactionRepository;

        @Cacheable(value = "account-balance", key = "#accountId")
        public AccountBalanceResponse getBalance(UUID accountId, UUID userId) {
                Account account = accountRepository.findByIdAndUserId(accountId, userId)
                                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

                long balance = calculateBalance(account);
                return new AccountBalanceResponse(accountId, balance, account.getCurrency());
        }

        @CacheEvict(value = "account-balance", key = "#accountId")
        public void evictBalance(UUID accountId) {
                // chamado quando uma transação da conta é alterada
        }

        public SummaryResponse getSummary(UUID userId, LocalDate from, LocalDate to) {
                List<Transaction> transactions = transactionRepository
                                .findByUserIdAndOccurredOnBetweenOrderByOccurredOnDesc(userId, from, to);

                long totalIncome = transactions.stream()
                                .filter(t -> t.getStatus() == TransactionStatus.CONFIRMED
                                                && t.getType() == TransactionType.INCOME)
                                .mapToLong(Transaction::getAmountMinor)
                                .sum();

                long totalExpense = transactions.stream()
                                .filter(t -> t.getStatus() == TransactionStatus.CONFIRMED
                                                && t.getType() == TransactionType.EXPENSE)
                                .mapToLong(Transaction::getAmountMinor)
                                .sum();

                Map<UUID, Long> byCategory = transactions.stream()
                                .filter(t -> t.getStatus() == TransactionStatus.CONFIRMED
                                                && t.getType() == TransactionType.EXPENSE
                                                && t.getCategoryId() != null)
                                .collect(Collectors.groupingBy(
                                                Transaction::getCategoryId,
                                                Collectors.summingLong(Transaction::getAmountMinor)));

                return new SummaryResponse(from, to, totalIncome, totalExpense, totalIncome - totalExpense, byCategory);
        }

        private long calculateBalance(Account account) {
                List<Transaction> incoming = transactionRepository
                                .findByAccountIdAndStatus(account.getId(), TransactionStatus.CONFIRMED);
                List<Transaction> transfersReceived = transactionRepository
                                .findByTransferAccountIdAndStatus(account.getId(), TransactionStatus.CONFIRMED);

                long balance = account.getOpeningBalanceMinor();
                for (Transaction tx : incoming) {
                        balance += switch (tx.getType()) {
                                case INCOME -> tx.getAmountMinor();
                                case EXPENSE -> -tx.getAmountMinor();
                                case TRANSFER -> -tx.getAmountMinor(); // saída da conta de origem
                        };
                }
                for (Transaction tx : transfersReceived) {
                        balance += tx.getAmountMinor(); // entrada na conta de destino
                }
                return balance;
        }
}
