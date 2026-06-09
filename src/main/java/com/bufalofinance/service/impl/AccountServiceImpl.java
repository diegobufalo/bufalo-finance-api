package com.bufalofinance.service.impl;

import com.bufalofinance.dto.request.AccountRequest;
import com.bufalofinance.dto.response.AccountResponse;
import com.bufalofinance.entity.Account;
import com.bufalofinance.repository.AccountRepository;
import com.bufalofinance.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<AccountResponse> findAll(UUID userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(AccountResponse::from)
                .toList();
    }

    @Override
    public AccountResponse findById(UUID id, UUID userId) {
        return accountRepository.findByIdAndUserId(id, userId)
                .map(AccountResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    @Override
    @Transactional
    public AccountResponse create(AccountRequest req, UUID userId) {
        Account account = new Account();
        applyRequest(account, req);
        account.setUserId(userId);
        return AccountResponse.from(accountRepository.save(account));
    }

    @Override
    @Transactional
    public AccountResponse update(UUID id, AccountRequest req, UUID userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        applyRequest(account, req);
        return AccountResponse.from(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
        accountRepository.delete(account);
    }

    private void applyRequest(Account account, AccountRequest req) {
        account.setName(req.name());
        account.setType(req.type());
        account.setCurrency(req.currency() != null ? req.currency() : "BRL");
        account.setColor(req.color());
        account.setIcon(req.icon());
        account.setOpeningBalanceMinor(req.openingBalanceMinor());
        account.setCreditLimitMinor(req.creditLimitMinor());
        account.setClosingDay(req.closingDay());
        account.setDueDay(req.dueDay());
    }
}
