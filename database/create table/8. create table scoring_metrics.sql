drop table if exists scoring_metric;
CREATE TABLE scoring_metric
(
    scoring_metric_id   uuid PRIMARY KEY,
    stage_name          varchar(50) NULL,
    metric_name         varchar(50) NULL,
    metric_weight       float NULL
);