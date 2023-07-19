drop table if exists teacher;
create table teacher
(
    teacher_id               uuid PRIMARY KEY,
    team_name                varchar(50) NULL,
    is_registered            BOOL DEFAULT FALSE
);