package com.bufalofinance.entity;

import com.bufalofinance.common.BaseEntity;
import com.bufalofinance.entity.enums.TransactionSource;
import com.bufalofinance.entity.enums.TransactionStatus;
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
@Table(name = "transactions")
@SQLDelete(sql = "UPDATE transactions SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "transfer_account_id")
    private UUID transferAccountId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "recurrence_id")
    private UUID recurrenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionStatus status = TransactionStatus.CONFIRMED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TransactionSource source = TransactionSource.MANUAL;

    @Column(name = "amount_minor", nullable = false)
    private long amountMinor;

    @Column(nullable = false, length = 3, columnDefinition = "char(3) default 'BRL'")
    private String currency = "BRL";

    @Column(length = 255)
    private String description;

    @Column(name = "occurred_on", nullable = false)
    private LocalDate occurredOn;

    @Column(name = "external_id", length = 120)
    private String externalId;
}
