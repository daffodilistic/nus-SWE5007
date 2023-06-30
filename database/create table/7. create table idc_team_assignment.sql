drop table if exists idc_team_assignment;
create table idc_team_assignment
(
    idc_team_assignment_id      uuid PRIMARY KEY,
    user_id                     uuid REFERENCES users (user_id),
    idc_team_id                 uuid REFERENCES idc_team (idc_team_id)
);