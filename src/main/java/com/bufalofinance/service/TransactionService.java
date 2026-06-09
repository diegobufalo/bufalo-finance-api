package com.bufalofinance.service;

import com.bufalofinance.dto.request.TransactionRequest;
import com.bufalofinance.dto.response.TransactionResponse;
import com.bufalofinance.entity.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionResponse> findAll(UUID userId, LocalDate from, LocalDate to,
            UUID accountId, UUID categoryId, TransactionType type);

    TransactionResponse findById(UUID id, UUID userId);

    TransactionResponse create(TransactionRequest request, UUID userId);

    TransactionResponse update(UUID id, TransactionRequest request, UUID userId);

    void delete(UUID id, UUID userId);
}
