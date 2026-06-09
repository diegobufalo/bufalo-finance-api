package com.bufalofinance.entity;

import com.bufalofinance.common.BaseEntity;
import com.bufalofinance.entity.enums.CategoryKind;
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
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 80)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CategoryKind kind;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(length = 16)
    private String color;

    @Column(length = 40)
    private String icon;
}
