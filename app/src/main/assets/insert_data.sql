-- Clear data from previous sessions
DELETE FROM exercise;
DELETE FROM tag;
DELETE FROM exercise_tag;


-- Insert sample exercises
INSERT INTO PUBLIC.exercise VALUES (
  1,
  'Push-Up',
  'Start in a plank position.' || CHAR(13) ||
  'Lower your body until your chest nearly touches the floor.' || CHAR(13) ||
  'Push back up to the starting position.',
  'images/push_up.png',
  FALSE,
  FALSE
);

INSERT INTO PUBLIC.exercise VALUES (
  2,
  'Squat',
  'Stand with feet shoulder-width apart.' || CHAR(13) ||
  'Lower your body by bending your knees until your thighs are parallel to the floor.' || CHAR(13) ||
  'Push back up to the starting position.',
  'images/squat.png',
  FALSE,
  TRUE
);

INSERT INTO PUBLIC.exercise VALUES (
  3,
  'Plank',
  'Hold a push-up position with your arms straight.' || CHAR(13) ||
  'Keep your body in a straight line from head to heels.' || CHAR(13) ||
  'Engage your core and hold the position.',
  'images/plank.png',
  TRUE,
  FALSE
);

INSERT INTO PUBLIC.exercise VALUES (
  4,
  'Pull-Up',
  'Grip a pull-up bar with palms facing away.' || CHAR(13) ||
  'Pull yourself up until your chin clears the bar.' || CHAR(13) ||
  'Lower yourself back down to the starting position.',
  'images/pull-up.png',
  FALSE,
  FALSE
);

INSERT INTO PUBLIC.exercise VALUES (
  5,
  'Lunges',
  'Step forward with one leg.' || CHAR(13) ||
  'Lower your hips until both knees are bent at 90-degree angles.' || CHAR(13) ||
  'Push back up to the starting position and repeat with the other leg.',
  'images/lunges.png',
  FALSE,
  FALSE
);


-- Insert sample tags
-- Muscle Groups
INSERT INTO PUBLIC.tag VALUES (1, 'Upper Body', 1, '#1D4ED8', '#D1E8FF');
INSERT INTO PUBLIC.tag VALUES (2, 'Lower Body', 1, '#6B21A8', '#E9D8FD');
INSERT INTO PUBLIC.tag VALUES (3, 'Core', 1, '#0D9488', '#B2F5EA');
INSERT INTO PUBLIC.tag VALUES (4, 'Full Body', 1, '#15803D', '#DCFCE7');

-- Difficulty Levels
INSERT INTO PUBLIC.tag VALUES (5, 'Beginner', 0, '#10B981', '#DCFCE7');
INSERT INTO PUBLIC.tag VALUES (6, 'Intermediate', 0, '#F59E0B', '#FEF3C7');
INSERT INTO PUBLIC.tag VALUES (7, 'Advanced', 0, '#DC2626', '#FEE2E2');

-- Exercise Types
INSERT INTO PUBLIC.tag VALUES (8, 'Cardio', 2, '#D97706', '#FFEDD5');
INSERT INTO PUBLIC.tag VALUES (9, 'Strength', 2, '#DB2777', '#FFD6E8');
INSERT INTO PUBLIC.tag VALUES (10, 'Flexibility', 2, '#059669', '#D1FAE5');
INSERT INTO PUBLIC.tag VALUES (11, 'Balance', 2, '#4338CA', '#E0E7FF');
INSERT INTO PUBLIC.tag VALUES (12, 'Endurance', 2, '#DB2777', '#FFE4E6');

-- Equipment
INSERT INTO PUBLIC.tag VALUES (13, 'No Equipment', 3, '#1E3A8A', '#E0F2FE');
INSERT INTO PUBLIC.tag VALUES (14, 'Dumbbells', 3, '#BE185D', '#FCE7F3');
INSERT INTO PUBLIC.tag VALUES (15, 'Resistance Bands', 3, '#D97706', '#FEF3C7');


-- Insert links between exercises and tags
INSERT INTO PUBLIC.exercise_tag VALUES (1, 1);
INSERT INTO PUBLIC.exercise_tag VALUES (1, 2);
INSERT INTO PUBLIC.exercise_tag VALUES (1, 3);
INSERT INTO PUBLIC.exercise_tag VALUES (1, 4);
INSERT INTO PUBLIC.exercise_tag VALUES (1, 15);
INSERT INTO PUBLIC.exercise_tag VALUES (2, 5);
INSERT INTO PUBLIC.exercise_tag VALUES (2, 7);
INSERT INTO PUBLIC.exercise_tag VALUES (2, 4);
INSERT INTO PUBLIC.exercise_tag VALUES (2, 6);
INSERT INTO PUBLIC.exercise_tag VALUES (2, 15);
INSERT INTO PUBLIC.exercise_tag VALUES (3, 4);
INSERT INTO PUBLIC.exercise_tag VALUES (3, 3);
INSERT INTO PUBLIC.exercise_tag VALUES (3, 8);
INSERT INTO PUBLIC.exercise_tag VALUES (3, 13);
INSERT INTO PUBLIC.exercise_tag VALUES (3, 14);
INSERT INTO PUBLIC.exercise_tag VALUES (4, 11);
INSERT INTO PUBLIC.exercise_tag VALUES (4, 15);
INSERT INTO PUBLIC.exercise_tag VALUES (4, 9);
INSERT INTO PUBLIC.exercise_tag VALUES (4, 10);
INSERT INTO PUBLIC.exercise_tag VALUES (5, 11);
INSERT INTO PUBLIC.exercise_tag VALUES (5, 15);
INSERT INTO PUBLIC.exercise_tag VALUES (5, 9);
INSERT INTO PUBLIC.exercise_tag VALUES (5, 10);
