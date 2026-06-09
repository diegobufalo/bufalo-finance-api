package com.bufalofinance.service.impl;

import com.bufalofinance.dto.request.RecurrenceRequest;
import com.bufalofinance.dto.response.RecurrenceResponse;
import com.bufalofinance.entity.RecurrenceRule;
import com.bufalofinance.repository.RecurrenceRuleRepository;
import com.bufalofinance.service.RecurrenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecurrenceServiceImpl implements RecurrenceService {

    private final RecurrenceRuleRepository recurrenceRuleRepository;

    @Override
    public List<RecurrenceResponse> findAll(UUID userId) {
        return recurrenceRuleRepository.findByUserId(userId).stream()
                .map(RecurrenceResponse::from)
                .toList();
    }

    @Override
    public RecurrenceResponse findById(UUID id, UUID userId) {
        return recurrenceRuleRepository.findByIdAndUserId(id, userId)
                .map(RecurrenceResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Recorrência não encontrada"));
    }

    @Override
    @Transactional
    public RecurrenceResponse create(RecurrenceRequest req, UUID userId) {
        RecurrenceRule rule = new RecurrenceRule();
        applyRequest(rule, req);
        rule.setUserId(userId);
        return RecurrenceResponse.from(recurrenceRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public RecurrenceResponse update(UUID id, RecurrenceRequest req, UUID userId) {
        RecurrenceRule rule = recurrenceRuleRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Recorrência não encontrada"));
        applyRequest(rule, req);
        return RecurrenceResponse.from(recurrenceRuleRepository.save(rule));
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId) {
        RecurrenceRule rule = recurrenceRuleRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Recorrência não encontrada"));
        recurrenceRuleRepository.delete(rule);
    }

    private void applyRequest(RecurrenceRule rule, RecurrenceRequest req) {
        rule.setAccountId(req.accountId());
        rule.setCategoryId(req.categoryId());
        rule.setType(req.type());
        rule.setDescription(req.description());
        rule.setAmountMinor(req.amountMinor());
        rule.setCurrency(req.currency() != null ? req.currency() : "BRL");
        rule.setFrequency(req.frequency());
        rule.setIntervalCount(req.intervalCount() > 0 ? req.intervalCount() : 1);
        rule.setDayOfMonth(req.dayOfMonth());
        rule.setDayOfWeek(req.dayOfWeek());
        rule.setStartDate(req.startDate());
        rule.setEndDate(req.endDate());
    }
}
