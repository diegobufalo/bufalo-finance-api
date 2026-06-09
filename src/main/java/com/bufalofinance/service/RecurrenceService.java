package com.bufalofinance.service;

import com.bufalofinance.dto.request.RecurrenceRequest;
import com.bufalofinance.dto.response.RecurrenceResponse;

import java.util.List;
import java.util.UUID;

public interface RecurrenceService {
    List<RecurrenceResponse> findAll(UUID userId);

    RecurrenceResponse findById(UUID id, UUID userId);

    RecurrenceResponse create(RecurrenceRequest request, UUID userId);

    RecurrenceResponse update(UUID id, RecurrenceRequest request, UUID userId);

    void delete(UUID id, UUID userId);
}
