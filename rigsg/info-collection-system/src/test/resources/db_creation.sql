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

drop table if exists `idc_team`;
create table idc_team
(
    id                        uuid not NULL,
    team_name                 varchar(255) NULL,
    competition_choice        varchar(255) NULL,
    age_group                 varchar(255) NULL,
    idc_group_id              uuid NULL,
    team_score_first_stage    BIGINT NULL,
    team_score_second_stage   BIGINT NULL,
    is_qualified_second_stage BOOL NULL,

    CONSTRAINT idc_team_pkey PRIMARY KEY (id)
);

