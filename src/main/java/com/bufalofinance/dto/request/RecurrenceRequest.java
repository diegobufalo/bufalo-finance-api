package com.bufalofinance.dto.request;

import com.bufalofinance.entity.enums.RecurrenceFrequency;
import com.bufalofinance.entity.enums.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record RecurrenceRequest(
        @NotNull UUID accountId,
        UUID categoryId,
        @NotNull TransactionType type,
        @Size(max = 255) String description,
        @Positive long amountMinor,
        @Size(min = 3, max = 3) String currency,
        @NotNull RecurrenceFrequency frequency,
        @Min(1) int intervalCount,
        Short dayOfMonth,
        Short dayOfWeek,
        @NotNull LocalDate startDate,
        LocalDate endDate
) {}
