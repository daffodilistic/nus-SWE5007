drop table if exists idc_team;
create table idc_team
(
    idc_team_id               uuid PRIMARY KEY,
    idc_group_id              uuid REFERENCES idc_group (idc_group_id),
    team_name                 varchar(50) NULL,
    age_group                 varchar(10) NULL,
    rank_first_stage    BIGINT NULL,
    is_qualified_promo  BOOL DEFAULT FALSE,
    is_qualified_final  BOOL DEFAULT FALSE,
    is_qualified_final_second_stage  BOOL DEFAULT FALSE,
    teacher_id                uuid REFERENCES teacher (teacher_id)
);