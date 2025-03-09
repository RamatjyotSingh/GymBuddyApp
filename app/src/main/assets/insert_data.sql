-- Clear data from previous sessions (order matters for foreign key constraints)
DELETE FROM PUBLIC.session_item;
DELETE FROM PUBLIC.workout_session;
DELETE FROM PUBLIC.profile_exercise;
DELETE FROM PUBLIC.exercise_tag;
DELETE FROM PUBLIC.workout_profile; 
DELETE FROM PUBLIC.tag;
DELETE FROM PUBLIC.exercise;


-- Insert sample exercises
INSERT INTO PUBLIC.exercise (exercise_id, name, instructions, image_path, is_time_based, has_weight) VALUES 
(1, 'Push-Up', 'Start in a plank position. Lower your body until your chest nearly touches the floor. Push back up to the starting position.', 'images/push_up.png', FALSE, FALSE),
(2, 'Squat', 'Stand with feet shoulder-width apart. Lower your body by bending your knees. Push back up to the starting position.', 'images/squat.png', FALSE, TRUE),
(3, 'Plank', 'Hold a push-up position with your arms straight. Keep your body in a straight line from head to heels. Engage your core and hold.', 'images/plank.png', TRUE, FALSE),
(4, 'Bench Press', 'Lie on a bench with feet flat on the floor. Grip the barbell with hands slightly wider than shoulder-width. Lower the bar to your chest, then press back up.', 'images/bench_press.png', FALSE, TRUE),
(5, 'Deadlift', 'Stand with feet hip-width apart. Bend at the hips and knees to grip the barbell. Keeping your back straight, stand up tall, then return the weight to the ground.', 'images/deadlift.png', FALSE, TRUE);


-- Insert sample tags
INSERT INTO PUBLIC.tag (tag_id, tag_name, tag_type, text_color, background_color) VALUES
(1, 'Upper Body', 1, '#FFFFFF', '#1D4ED8'),
(2, 'Lower Body', 1, '#FFFFFF', '#6B21A8'),
(3, 'Core', 1, '#FFFFFF', '#0D9488'),
(4, 'Beginner', 0, '#FFFFFF', '#15803D'),
(5, 'Intermediate', 0, '#FFFFFF', '#CA8A04'),
(6, 'Advanced', 0, '#FFFFFF', '#BE185D');


-- Link exercises to tags
INSERT INTO PUBLIC.exercise_tag (exercise_id, tag_id) VALUES
(1, 1), -- Push-up: Upper Body
(1, 4), -- Push-up: Beginner
(2, 2), -- Squat: Lower Body
(2, 4), -- Squat: Beginner
(3, 3), -- Plank: Core
(3, 4), -- Plank: Beginner
(4, 1), -- Bench Press: Upper Body
(4, 5), -- Bench Press: Intermediate
(5, 2), -- Deadlift: Lower Body
(5, 6); -- Deadlift: Advanced


-- Insert sample workout profiles
INSERT INTO PUBLIC.workout_profile (profile_id, profile_name, icon_path) VALUES
(1, 'Full Body Workout', 'icons/full_body.png'),
(2, 'Upper Body Focus', 'icons/upper_body.png'),
(3, 'Lower Body Day', 'icons/lower_body.png');


-- Add exercises to profiles
-- Full Body Workout
INSERT INTO PUBLIC.profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) VALUES
(1, 1, 3, 10, 0, 0),    -- Push-ups: 3 sets, 10 reps
(1, 2, 3, 12, 40, 0),   -- Squats: 3 sets, 12 reps, 40 lbs
(1, 3, 3, 0, 0, 60);    -- Plank: 3 sets, 60 seconds


-- Upper Body Focus
INSERT INTO PUBLIC.profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) VALUES
(2, 1, 4, 15, 0, 0),    -- Push-ups: 4 sets, 15 reps
(2, 4, 3, 8, 135, 0);   -- Bench Press: 3 sets, 8 reps, 135 lbs


-- Lower Body Day
INSERT INTO PUBLIC.profile_exercise (profile_id, exercise_id, sets, reps, weight, duration) VALUES
(3, 2, 4, 12, 60, 0),   -- Squats: 4 sets, 12 reps, 60 lbs
(3, 5, 3, 5, 225, 0);   -- Deadlift: 3 sets, 5 reps, 225 lbs


-- Insert a sample workout session
INSERT INTO PUBLIC.workout_session (session_id, start_time, end_time, profile_id) VALUES
(1, 1709473200000, 1709476800000, 1); -- Full Body Workout session


-- Insert exercises performed in the session
INSERT INTO PUBLIC.session_item (session_id, exercise_id, reps, weight, duration) VALUES
(1, 1, 10, 0, 0),       -- Push-ups: 10 reps
(1, 2, 12, 45, 0),      -- Squats: 12 reps, 45 lbs
(1, 3, 0, 0, 45);       -- Plank: 45 seconds
