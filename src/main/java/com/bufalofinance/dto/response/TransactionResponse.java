package com.bufalofinance.dto.response;

import com.bufalofinance.entity.Transaction;
import com.bufalofinance.entity.enums.TransactionSource;
import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.entity.enums.TransactionType;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID transferAccountId,
        UUID categoryId,
        UUID recurrenceId,
        TransactionType type,
        TransactionStatus status,
        TransactionSource source,
        long amountMinor,
        String currency,
        String description,
        LocalDate occurredOn
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
                t.getId(), t.getAccountId(), t.getTransferAccountId(),
                t.getCategoryId(), t.getRecurrenceId(), t.getType(),
                t.getStatus(), t.getSource(), t.getAmountMinor(),
                t.getCurrency(), t.getDescription(), t.getOccurredOn()
        );
    }
}
