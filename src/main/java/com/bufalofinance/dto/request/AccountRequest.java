package com.bufalofinance.dto.request;

import com.bufalofinance.entity.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull AccountType type,
        @Size(min = 3, max = 3) String currency,
        @Size(max = 16) String color,
        @Size(max = 40) String icon,
        long openingBalanceMinor,
        Long creditLimitMinor,
        Short closingDay,
        Short dueDay
) {}
