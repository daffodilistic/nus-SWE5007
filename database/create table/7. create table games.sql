drop table if exists games;
create table games
(
    game_id             uuid PRIMARY KEY,
    game_team_id_host   uuid REFERENCES game_team (game_team_id),
    game_team_id_oppo   uuid REFERENCES game_team (game_team_id),
    game_score_host     BIGINT NULL,
    game_score_oppo     BIGINT NULL,
    game_status         varchar(50)  NULL,
    game_outcome        varchar(50)  NULL,
    venue               varchar(50)  NULL,
    datetime            timestamp without time zone,
    stage               varchar(10)  NULL,
);

