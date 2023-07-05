-- test data for users
INSERT INTO users (user_id,first_name, last_name, email, country, state, date_of_birth, phone_number, school_name, years_of_exp, idc_team_id, game_team_id)
VALUES (uuid_generate_v1(),'John', 'Doe', 'johndoe@example.com', 'United States', 'California', '1990-01-01', '1234567890', 'ABC University', 5, '3fb3ff04-1adb-11ee-8988-42010a400017', '47409b00-1adc-11ee-8988-42010a400017');

-- test data for judge
INSERT INTO judge (judge_id, name )
VALUES (uuid_generate_v1(),'Prof RIC');

-- test data for idc group
INSERT INTO idc_group (idc_group_id, group_name, judge_id)
VALUES (uuid_generate_v1(),'IDC Group 1', 'a090c3f4-1750-11ee-8166-42010a400015');

-- test data for game group
INSERT INTO game_group (game_group_id, game_team_id_first, game_team_id_second)
VALUES (uuid_generate_v1(),'Game Team 1','Game Team 2');

-- test data for idc team
INSERT INTO idc_team (idc_team_id,idc_group_id,team_name, competition_choice, age_group, team_score_first_stage, team_score_second_stage,rank_first_stage)
VALUES (uuid_generate_v1(),'d82d66c8-1750-11ee-8166-42010a400015','Team ABC', 'IDC', '18', 280,300, 1);

-- test data for game team (need to separate two insert for different UUIDs)
INSERT INTO game_team (game_team_id,team_name, competition_choice, age_group, game_group_id,qualification_round_score, qualification_round_point, qualification_round_num_matches_played)
VALUES 
(uuid_generate_v1(), 'Team Garena', 'GrandArena', '21', 'f5767016-1751-11ee-8166-42010a400015',230, 7,3);

INSERT INTO game_team (game_team_id,team_name, competition_choice, age_group, game_group_id,qualification_round_score, qualification_round_point, qualification_round_num_matches_played)
VALUES
(uuid_generate_v1(), 'Team TikTok', 'GrandArena', '21', 'f5767016-1751-11ee-8166-42010a400015',200, 6,3);

-- test data for games (id_a, id_b is not static value - must be same as the 2 UUIDs generated above)
INSERT INTO games (game_id, game_team_id_A, game_team_id_B, game_score_A, game_score_B, game_status, game_outcome)
VALUES (uuid_generate_v1(),'47409b00-1adc-11ee-8988-42010a400017','5e115a04-1adc-11ee-8988-42010a400017',123,134,'test','test');

-- test data for scoring metrics
INSERT INTO scoring_metric (scoring_metric_id, stage_name, metric_name, metric_weight)
VALUES (uuid_generate_v1(),'IDC Stage 1','Proposal',0.3);
