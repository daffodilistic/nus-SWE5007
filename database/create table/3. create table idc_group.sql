drop table if exists idc_group;
create table idc_group
(
    idc_group_id    uuid PRIMARY KEY,
    group_name      varchar(255) NULL,
    judge_id        uuid REFERENCES judge (judge_id)
);