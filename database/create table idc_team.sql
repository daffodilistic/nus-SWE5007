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