package com.bufalofinance.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base de todas as entidades.
 * id UUID + auditoria (createdAt/updatedAt) + campo de soft delete.
 *
 * IMPORTANTE: o soft delete em si (@SQLDelete + @SQLRestriction) deve ser
 * declarado em CADA entidade concreta, pois depende do nome da tabela.
 * Ex. na entidade Account:
 * 
 * @SQLDelete(sql = "update accounts set deleted_at = now() where id = ?")
 *                @SQLRestriction("deleted_at is null")
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}
