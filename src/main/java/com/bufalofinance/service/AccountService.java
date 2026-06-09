package com.bufalofinance.service;

import com.bufalofinance.dto.request.AccountRequest;
import com.bufalofinance.dto.response.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountResponse> findAll(UUID userId);

    AccountResponse findById(UUID id, UUID userId);

    AccountResponse create(AccountRequest request, UUID userId);

    AccountResponse update(UUID id, AccountRequest request, UUID userId);

    void delete(UUID id, UUID userId);
}
