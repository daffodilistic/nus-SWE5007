drop table if exists game_data;
create table game_data
(
    data_id                     uuid PRIMARY KEY,
    game_name                   varchar(10) NULL,
    host_team                   varchar(50) NULL,
    host_school                 varchar(50) NULL,
    host_location               varchar(50) NULL,
    host_avg_exp                float NULL,
    host_score                  BIGINT NULL,
    oppo_team                   varchar(50) NULL,
    oppo_school                 varchar(50) NULL,
    oppo_location               varchar(50) NULL,
    oppo_avg_exp                float NULL,
    oppo_score                  BIGINT NULL,
    game_outcome                varchar(10) NULL,
    year_month                  varchar(10) NULL
);