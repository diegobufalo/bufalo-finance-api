package com.bufalofinance.dto.response;

import com.bufalofinance.entity.RecurrenceRule;
import com.bufalofinance.entity.enums.RecurrenceFrequency;
import com.bufalofinance.entity.enums.TransactionType;

import java.time.LocalDate;
import java.util.UUID;

public record RecurrenceResponse(
        UUID id,
        UUID accountId,
        UUID categoryId,
        TransactionType type,
        String description,
        long amountMinor,
        String currency,
        RecurrenceFrequency frequency,
        int intervalCount,
        Short dayOfMonth,
        Short dayOfWeek,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate lastGeneratedDate,
        boolean active
) {
    public static RecurrenceResponse from(RecurrenceRule r) {
        return new RecurrenceResponse(
                r.getId(), r.getAccountId(), r.getCategoryId(), r.getType(),
                r.getDescription(), r.getAmountMinor(), r.getCurrency(),
                r.getFrequency(), r.getIntervalCount(), r.getDayOfMonth(),
                r.getDayOfWeek(), r.getStartDate(), r.getEndDate(),
                r.getLastGeneratedDate(), r.isActive()
        );
    }
}
