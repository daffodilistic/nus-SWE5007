drop table if exists game_team_assignment;
create table game_team_assignment
(
    game_team_assignment_id     uuid PRIMARY KEY,
    user_id                     uuid REFERENCES users (user_id),
    game_team_id                uuid REFERENCES game_team (game_team_id)
);