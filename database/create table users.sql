drop table if exists `users`;
CREATE TABLE users
(
    id            uuid not NULL,
    first_name    varchar(255) NULL,
    last_name     varchar(255) NULL,
    email         varchar(255) NULL,
    country       varchar(255) NULL,
    state         varchar(255) NULL,
    date_of_birth varchar(255) NULL,
    phone_number  varchar(255) NULL,
    school_name   varchar(255) NULL,
    years_of_exp  BIGINT NULL,
    is_qualified  BOOL NULL,
    team_id       uuid NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);