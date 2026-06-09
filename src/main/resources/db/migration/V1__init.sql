-- Bufalo Finance — schema inicial (v1)
-- Decisões fixas: dinheiro em centavos (BIGINT), PK UUID, soft delete (deleted_at),
-- escopo por usuário, dedup de Open Finance via external_id.

create extension if not exists "pgcrypto"; -- habilita gen_random_uuid()

-- ===== USERS =====
create table users (
    id            uuid primary key default gen_random_uuid(),
    name          varchar(120) not null,
    email         varchar(255) not null unique,
    password_hash varchar(255),                 -- null se login só via Google (futuro)
    google_sub    varchar(255) unique,          -- subject do Google OAuth (futuro)
    invited_by    uuid references users(id),
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),
    deleted_at    timestamptz
);

-- Convites (app é invite-only) =====
create table invite_codes (
    code        varchar(40) primary key,
    created_by  uuid references users(id),
    used_by     uuid references users(id),
    used_at     timestamptz,
    expires_at  timestamptz,
    created_at  timestamptz not null default now()
);

-- ===== ACCOUNTS (carteiras / bancos) =====
create table accounts (
    id                    uuid primary key default gen_random_uuid(),
    user_id               uuid not null references users(id),
    name                  varchar(120) not null,
    type                  varchar(20) not null
                          check (type in ('WALLET','BANK_CHECKING','BANK_CREDIT')),
    currency              char(3) not null default 'BRL',
    color                 varchar(16),
    icon                  varchar(40),
    opening_balance_minor bigint not null default 0,
    -- somente para BANK_CREDIT:
    credit_limit_minor    bigint,
    closing_day           smallint check (closing_day between 1 and 31),
    due_day               smallint check (due_day between 1 and 31),
    archived              boolean not null default false,
    created_at            timestamptz not null default now(),
    updated_at            timestamptz not null default now(),
    deleted_at            timestamptz
);
create index idx_accounts_user on accounts(user_id) where deleted_at is null;

-- ===== CATEGORIES =====
create table categories (
    id          uuid primary key default gen_random_uuid(),
    user_id     uuid not null references users(id),
    name        varchar(80) not null,
    kind        varchar(10) not null check (kind in ('INCOME','EXPENSE')),
    parent_id   uuid references categories(id),
    color       varchar(16),
    icon        varchar(40),
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    deleted_at  timestamptz
);
create index idx_categories_user on categories(user_id) where deleted_at is null;

-- ===== RECURRENCE RULES =====
create table recurrence_rules (
    id                  uuid primary key default gen_random_uuid(),
    user_id             uuid not null references users(id),
    account_id          uuid not null references accounts(id),
    category_id         uuid references categories(id),
    type                varchar(10) not null check (type in ('INCOME','EXPENSE')),
    description         varchar(255),
    amount_minor        bigint not null,
    currency            char(3) not null default 'BRL',
    frequency           varchar(10) not null
                        check (frequency in ('DAILY','WEEKLY','MONTHLY','YEARLY')),
    interval_count      int not null default 1 check (interval_count >= 1),
    day_of_month        smallint check (day_of_month between 1 and 31),
    day_of_week         smallint check (day_of_week between 0 and 6), -- 0=domingo
    start_date          date not null,
    end_date            date,
    last_generated_date date,
    active              boolean not null default true,
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now(),
    deleted_at          timestamptz
);
create index idx_recurrence_user on recurrence_rules(user_id) where deleted_at is null;

-- ===== TRANSACTIONS =====
create table transactions (
    id                  uuid primary key default gen_random_uuid(),
    user_id             uuid not null references users(id),
    account_id          uuid not null references accounts(id),
    transfer_account_id uuid references accounts(id),     -- destino, só p/ TRANSFER
    category_id         uuid references categories(id),
    recurrence_id       uuid references recurrence_rules(id),
    type                varchar(10) not null
                        check (type in ('INCOME','EXPENSE','TRANSFER')),
    status              varchar(10) not null default 'CONFIRMED'
                        check (status in ('CONFIRMED','PROJECTED')),
    source              varchar(15) not null default 'MANUAL'
                        check (source in ('MANUAL','OPEN_FINANCE')),
    amount_minor        bigint not null,
    currency            char(3) not null default 'BRL',
    description         varchar(255),
    occurred_on         date not null,
    external_id         varchar(120),                     -- dedup Open Finance (v2)
    created_at          timestamptz not null default now(),
    updated_at          timestamptz not null default now(),
    deleted_at          timestamptz
);
create index idx_tx_user_date on transactions(user_id, occurred_on) where deleted_at is null;
create index idx_tx_account   on transactions(account_id)          where deleted_at is null;
-- evita importar a mesma transação do Open Finance duas vezes:
create unique index uq_tx_external on transactions(user_id, source, external_id)
    where external_id is not null and deleted_at is null;
