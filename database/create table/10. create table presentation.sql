drop table if exists presentation;
create table presentation
(
    presentation_id          uuid PRIMARY KEY,
    datetime                 timestamp without time zone,
    score                    float NULL,
    stage                    varchar(50) NULL,
    venue                    varchar(50) NULL,
    idc_team_id              uuid REFERENCES idc_team (idc_team_id)
);