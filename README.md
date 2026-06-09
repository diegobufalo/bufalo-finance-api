# Bufalo Finance

API de controle financeiro pessoal
web (React) agora, mobile (Expo) depois — ambos sobre a mesma API.

**Stack:** Java 25 · Spring Boot 3.4 · PostgreSQL · Redis · Flyway · Spring Security (JWT) · Maven.

## Pré-requisitos

- Java 25
- Docker (para subir Postgres + Redis) — ou Postgres/Redis locais
- Maven (ou rode `mvn -N wrapper:wrapper` para gerar o wrapper)

## Rodar localmente

```bash
docker compose up -d        # sobe Postgres (5432) e Redis (6379)
mvn spring-boot:run         # Flyway aplica o schema e a API sobe na :8080
```

## Estrutura

- `src/main/resources/db/migration/` — schema (Flyway é o dono do schema)
- `src/main/java/com/bufalofinance/` — código, organizado por domínio
- `docs/ARCHITECTURE.md` — modelo de dados, contrato da API e roadmap
