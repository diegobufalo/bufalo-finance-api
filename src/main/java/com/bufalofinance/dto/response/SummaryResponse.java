package com.bufalofinance.dto.response;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record SummaryResponse(
        LocalDate from,
        LocalDate to,
        long totalIncomeMinor,
        long totalExpenseMinor,
        long netMinor,
        Map<UUID, Long> expenseByCategory
) {}
