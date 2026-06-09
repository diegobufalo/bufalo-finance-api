package com.bufalofinance.controller;

import com.bufalofinance.dto.request.RecurrenceRequest;
import com.bufalofinance.dto.response.RecurrenceResponse;
import com.bufalofinance.service.RecurrenceService;
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
@RequestMapping("/api/recurrences")
@RequiredArgsConstructor
public class RecurrenceController {

    private final RecurrenceService recurrenceService;

    @GetMapping
    public List<RecurrenceResponse> list(Authentication auth) {
        return recurrenceService.findAll(userId(auth));
    }

    @GetMapping("/{id}")
    public RecurrenceResponse get(@PathVariable UUID id, Authentication auth) {
        return recurrenceService.findById(id, userId(auth));
    }

    @PostMapping
    public ResponseEntity<RecurrenceResponse> create(@Valid @RequestBody RecurrenceRequest request,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recurrenceService.create(request, userId(auth)));
    }

    @PutMapping("/{id}")
    public RecurrenceResponse update(@PathVariable UUID id,
            @Valid @RequestBody RecurrenceRequest request,
            Authentication auth) {
        return recurrenceService.update(id, request, userId(auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication auth) {
        recurrenceService.delete(id, userId(auth));
        return ResponseEntity.noContent().build();
    }

    private UUID userId(Authentication auth) {
        return (UUID) auth.getPrincipal();
    }
}
