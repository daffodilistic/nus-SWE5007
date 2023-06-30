drop table if exists game_team;
create table game_team
(
    game_team_id                                uuid PRIMARY KEY,
    team_name                                   varchar(50) NULL,
    competition_choice                          varchar(10) NULL,
    age_group                                   varchar(10) NULL,
    game_group_id                               uuid REFERENCES game_group (game_group_id),
    qualification_round_score                   BIGINT NULL,
    qualification_round_point                   BIGINT NULL,
    qualification_round_num_matches_played      BIGINT NULL,
    is_qualified_for_elimination                BOOL NULL
);