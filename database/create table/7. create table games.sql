drop table if exists games;
create table games
(
    game_id             uuid PRIMARY KEY,
    game_team_id_A      uuid REFERENCES game_team (game_team_id),
    game_team_id_B      uuid REFERENCES game_team (game_team_id),
    game_score_A        BIGINT NULL,
    game_score_B        BIGINT NULL,
    game_status         varchar(50)  NULL,
    game_outcome        varchar(50)  NULL
);

