drop table if exists game_group;
create table game_group
(
    game_group_id       uuid PRIMARY KEY,
    game_team_id_first    varchar(50) NULL,
    game_team_id_second    varchar(50) NULL
);