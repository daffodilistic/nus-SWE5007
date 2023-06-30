drop table if exists users;
CREATE TABLE users
(
    user_id             uuid PRIMARY KEY,
    first_name          varchar(50) NULL,
    last_name           varchar(50) NULL,
    email               varchar(128) NULL,
    country             varchar(50) NULL,
    state               varchar(50) NULL,
    date_of_birth       varchar(50) NULL,
    phone_number        varchar(20) NULL,
    school_name         varchar(50) NULL,
    years_of_exp        BIGINT NULL,
    is_qualified_prim   BOOL NULL,
    is_qualified_promo  BOOL NULL
);