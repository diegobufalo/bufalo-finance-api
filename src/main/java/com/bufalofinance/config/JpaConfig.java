package com.bufalofinance.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Liga auditoria JPA (preenche createdAt/updatedAt no BaseEntity),
 * o agendador (job de materialização de recorrência) e o cache (Redis).
 */
@Configuration
@EnableJpaAuditing
@EnableScheduling
@EnableCaching
public class JpaConfig {
}
