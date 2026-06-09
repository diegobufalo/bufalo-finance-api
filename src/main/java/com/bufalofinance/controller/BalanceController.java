package com.bufalofinance.controller;

import com.bufalofinance.dto.response.AccountBalanceResponse;
import com.bufalofinance.dto.response.SummaryResponse;
import com.bufalofinance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/api/accounts/{id}/balance")
    public AccountBalanceResponse balance(@PathVariable UUID id, Authentication auth) {
        return balanceService.getBalance(id, userId(auth));
    }

    @GetMapping("/api/reports/summary")
    public SummaryResponse summary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Authentication auth) {
        LocalDate effectiveFrom = from != null ? from : LocalDate.now().withDayOfMonth(1);
        LocalDate effectiveTo = to != null ? to : LocalDate.now();
        return balanceService.getSummary(userId(auth), effectiveFrom, effectiveTo);
    }

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }
}
