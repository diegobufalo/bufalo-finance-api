package com.bufalofinance.job;

import com.bufalofinance.entity.RecurrenceRule;
import com.bufalofinance.entity.Transaction;
import com.bufalofinance.entity.enums.TransactionSource;
import com.bufalofinance.entity.enums.TransactionStatus;
import com.bufalofinance.repository.RecurrenceRuleRepository;
import com.bufalofinance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class RecurrenceMaterializationJob {

    private final RecurrenceRuleRepository recurrenceRuleRepository;
    private final TransactionRepository transactionRepository;

    @Value("${app.recurrence.horizon-months:12}")
    private int horizonMonths;

    @Scheduled(cron = "0 0 2 * * *") // diariamente às 02:00
    @Transactional
    public void materialize() {
        LocalDate horizon = LocalDate.now().plusMonths(horizonMonths);
        List<RecurrenceRule> rules = recurrenceRuleRepository.findByActiveTrue();
        int created = 0;
        for (RecurrenceRule rule : rules) {
            created += materializeRule(rule, horizon);
        }
        if (created > 0) {
            log.info("Materialização de recorrências: {} transações PROJECTED criadas", created);
        }
    }

    private int materializeRule(RecurrenceRule rule, LocalDate horizon) {
        LocalDate cursor = nextDate(rule);
        LocalDate limit = rule.getEndDate() != null
                ? (rule.getEndDate().isBefore(horizon) ? rule.getEndDate() : horizon)
                : horizon;

        List<Transaction> toSave = new ArrayList<>();
        while (!cursor.isAfter(limit)) {
            Transaction tx = new Transaction();
            tx.setUserId(rule.getUserId());
            tx.setAccountId(rule.getAccountId());
            tx.setCategoryId(rule.getCategoryId());
            tx.setRecurrenceId(rule.getId());
            tx.setType(rule.getType());
            tx.setStatus(TransactionStatus.PROJECTED);
            tx.setSource(TransactionSource.MANUAL);
            tx.setAmountMinor(rule.getAmountMinor());
            tx.setCurrency(rule.getCurrency());
            tx.setDescription(rule.getDescription());
            tx.setOccurredOn(cursor);
            toSave.add(tx);
            cursor = advance(cursor, rule);
        }

        if (!toSave.isEmpty()) {
            transactionRepository.saveAll(toSave);
            rule.setLastGeneratedDate(toSave.getLast().getOccurredOn());
            recurrenceRuleRepository.save(rule);
        }
        return toSave.size();
    }

    private LocalDate nextDate(RecurrenceRule rule) {
        if (rule.getLastGeneratedDate() != null) {
            return advance(rule.getLastGeneratedDate(), rule);
        }
        return rule.getStartDate();
    }

    private LocalDate advance(LocalDate date, RecurrenceRule rule) {
        return switch (rule.getFrequency()) {
            case DAILY -> date.plusDays(rule.getIntervalCount());
            case WEEKLY -> date.plusWeeks(rule.getIntervalCount());
            case MONTHLY -> date.plusMonths(rule.getIntervalCount());
            case YEARLY -> date.plusYears(rule.getIntervalCount());
        };
    }
}
