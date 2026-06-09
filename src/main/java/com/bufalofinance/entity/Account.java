package com.bufalofinance.entity;

import com.bufalofinance.common.BaseEntity;
import com.bufalofinance.entity.enums.AccountType;
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

import java.util.UUID;

@Entity
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    @Column(nullable = false, length = 3, columnDefinition = "char(3) default 'BRL'")
    private String currency = "BRL";

    @Column(length = 16)
    private String color;

    @Column(length = 40)
    private String icon;

    @Column(name = "opening_balance_minor", nullable = false)
    private long openingBalanceMinor = 0;

    @Column(name = "credit_limit_minor")
    private Long creditLimitMinor;

    @Column(name = "closing_day")
    private Short closingDay;

    @Column(name = "due_day")
    private Short dueDay;

    @Column(nullable = false)
    private boolean archived = false;
}
