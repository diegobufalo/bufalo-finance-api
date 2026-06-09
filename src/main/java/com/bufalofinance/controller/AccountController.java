package com.bufalofinance.controller;

import com.bufalofinance.dto.request.AccountRequest;
import com.bufalofinance.dto.response.AccountResponse;
import com.bufalofinance.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponse> list(Authentication auth) {
        return accountService.findAll(userId(auth));
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable UUID id, Authentication auth) {
        return accountService.findById(id, userId(auth));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.create(request, userId(auth)));
    }

    @PutMapping("/{id}")
    public AccountResponse update(@PathVariable UUID id,
            @Valid @RequestBody AccountRequest request,
            Authentication auth) {
        return accountService.update(id, request, userId(auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication auth) {
        accountService.delete(id, userId(auth));
        return ResponseEntity.noContent().build();
    }

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }
}
