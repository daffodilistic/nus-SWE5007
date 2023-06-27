-- test data for users
INSERT INTO users (id,first_name, last_name, email, country, state, date_of_birth, phone_number, school_name, years_of_exp, is_qualified)
VALUES (uuid_generate_v1(),'John', 'Doe', 'johndoe@example.com', 'United States', 'California', '1990-01-01', '1234567890', 'ABC University', 5, true, 1);

-- test data for idc team
INSERT INTO idc_team (id,team_name, competition_choice, age_group, idc_group_id, team_score_first_stage, team_score_second_stage, is_qualified_second_stage)
VALUES (uuid_generate_v1(),'Team NTU', 'IDC', '18', NULL, 2, 2,true);

-- test data for game team
INSERT INTO game_team (id,team_name, competition_choice, age_group, qualification_round_score, qualification_round_point, qualification_round_num_matches_played, game_group_id, is_qualified_for_elimination)
VALUES (uuid_generate_v1(), 'Team Garena', 'Grand Arena', '18', 230, 7,3, NULL, true);

