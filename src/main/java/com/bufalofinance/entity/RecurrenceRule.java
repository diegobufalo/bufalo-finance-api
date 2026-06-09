package com.bufalofinance.entity;

import com.bufalofinance.common.BaseEntity;
import com.bufalofinance.entity.enums.RecurrenceFrequency;
import com.bufalofinance.entity.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "recurrence_rules")
@SQLDelete(sql = "UPDATE recurrence_rules SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Getter
@Setter
@NoArgsConstructor
public class RecurrenceRule extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @Column(length = 255)
    private String description;

    @Column(name = "amount_minor", nullable = false)
    private long amountMinor;

    @Column(nullable = false, length = 3, columnDefinition = "char(3) default 'BRL'")
    private String currency = "BRL";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RecurrenceFrequency frequency;

    @Column(name = "interval_count", nullable = false)
    private int intervalCount = 1;

    @Column(name = "day_of_month")
    private Short dayOfMonth;

    @Column(name = "day_of_week")
    private Short dayOfWeek;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "last_generated_date")
    private LocalDate lastGeneratedDate;

    @Column(nullable = false)
    private boolean active = true;
}
