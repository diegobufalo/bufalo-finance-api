package com.bufalofinance.dto.response;

import com.bufalofinance.entity.Account;
import com.bufalofinance.entity.enums.AccountType;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        String name,
        AccountType type,
        String currency,
        String color,
        String icon,
        long openingBalanceMinor,
        Long creditLimitMinor,
        Short closingDay,
        Short dueDay,
        boolean archived
) {
    public static AccountResponse from(Account a) {
        return new AccountResponse(
                a.getId(), a.getName(), a.getType(), a.getCurrency(),
                a.getColor(), a.getIcon(), a.getOpeningBalanceMinor(),
                a.getCreditLimitMinor(), a.getClosingDay(), a.getDueDay(),
                a.isArchived()
        );
    }
}
