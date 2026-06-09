package com.bufalofinance.dto.response;

import java.util.UUID;

public record AccountBalanceResponse(UUID accountId, long balanceMinor, String currency) {}
