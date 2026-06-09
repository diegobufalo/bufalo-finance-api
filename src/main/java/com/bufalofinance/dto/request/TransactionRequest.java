package com.bufalofinance.dto.request;

import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.entity.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionRequest(
        @NotNull UUID accountId,
        UUID transferAccountId,
        UUID categoryId,
        @NotNull TransactionType type,
        TransactionStatus status,
        @Positive long amountMinor,
        @Size(min = 3, max = 3) String currency,
        @Size(max = 255) String description,
        @NotNull LocalDate occurredOn
) {}
