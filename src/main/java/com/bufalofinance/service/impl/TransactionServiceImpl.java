package com.bufalofinance.service.impl;

import com.bufalofinance.dto.request.TransactionRequest;
import com.bufalofinance.dto.response.TransactionResponse;
import com.bufalofinance.entity.Transaction;
import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.entity.enums.TransactionType;
import com.bufalofinance.repository.TransactionRepository;
import com.bufalofinance.service.BalanceService;
import com.bufalofinance.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;

    @Override
    public List<TransactionResponse> findAll(UUID userId, LocalDate from, LocalDate to,
            UUID accountId, UUID categoryId, TransactionType type) {
        if (accountId != null) {
            return transactionRepository
                    .findByUserIdAndAccountIdAndOccurredOnBetweenOrderByOccurredOnDesc(userId, accountId, from, to)
                    .stream().map(TransactionResponse::from).toList();
        }
        if (categoryId != null) {
            return transactionRepository
                    .findByUserIdAndCategoryIdAndOccurredOnBetweenOrderByOccurredOnDesc(userId, categoryId, from, to)
                    .stream().map(TransactionResponse::from).toList();
        }
        if (type != null) {
            return transactionRepository
                    .findByUserIdAndTypeAndOccurredOnBetweenOrderByOccurredOnDesc(userId, type, from, to)
                    .stream().map(TransactionResponse::from).toList();
        }
        return transactionRepository
                .findByUserIdAndOccurredOnBetweenOrderByOccurredOnDesc(userId, from, to)
                .stream().map(TransactionResponse::from).toList();
    }

    @Override
    public TransactionResponse findById(UUID id, UUID userId) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
    }

    @Override
    @Transactional
    public TransactionResponse create(TransactionRequest req, UUID userId) {
        validate(req);
        Transaction tx = new Transaction();
        applyRequest(tx, req);
        tx.setUserId(userId);
        TransactionResponse response = TransactionResponse.from(transactionRepository.save(tx));
        evictAffectedBalances(tx);
        return response;
    }

    @Override
    @Transactional
    public TransactionResponse update(UUID id, TransactionRequest req, UUID userId) {
        Transaction tx = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
        validate(req);
        evictAffectedBalances(tx);
        applyRequest(tx, req);
        TransactionResponse response = TransactionResponse.from(transactionRepository.save(tx));
        evictAffectedBalances(tx);
        return response;
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId) {
        Transaction tx = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));
        evictAffectedBalances(tx);
        transactionRepository.delete(tx);
    }

    private void evictAffectedBalances(Transaction tx) {
        balanceService.evictBalance(tx.getAccountId());
        if (tx.getTransferAccountId() != null) {
            balanceService.evictBalance(tx.getTransferAccountId());
        }
    }

    private void applyRequest(Transaction tx, TransactionRequest req) {
        tx.setAccountId(req.accountId());
        tx.setTransferAccountId(req.transferAccountId());
        tx.setCategoryId(req.categoryId());
        tx.setType(req.type());
        tx.setStatus(req.status() != null ? req.status() : TransactionStatus.CONFIRMED);
        tx.setAmountMinor(req.amountMinor());
        tx.setCurrency(req.currency() != null ? req.currency() : "BRL");
        tx.setDescription(req.description());
        tx.setOccurredOn(req.occurredOn());
    }

    private void validate(TransactionRequest req) {
        if (req.type() == TransactionType.TRANSFER && req.transferAccountId() == null) {
            throw new IllegalArgumentException("transferAccountId é obrigatório para transferências");
        }
        if (req.type() == TransactionType.TRANSFER && req.accountId().equals(req.transferAccountId())) {
            throw new IllegalArgumentException("Conta de origem e destino devem ser diferentes");
        }
    }
}
