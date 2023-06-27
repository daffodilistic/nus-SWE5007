drop table if exists game_team;
create table game_team
(
    id                                          uuid not NULL,
    team_name                                   varchar(255) NULL,
    competition_choice                          varchar(255) NULL,
    age_group                                   varchar(255) NULL,
    qualification_round_score                   BIGINT NULL,
    qualification_round_point                   BIGINT NULL,
    qualification_round_num_matches_played      BIGINT NULL,
    game_group_id                               uuid NULL,
    is_qualified_for_elimination                BOOL NULL,

    CONSTRAINT game_team_pkey PRIMARY KEY (id)
);

