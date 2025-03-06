-- Insert sample exercises
INSERT INTO PUBLIC.EXERCISE VALUES (
  1,
  'Push-Up',
  'Start in a plank position.' ||
  '\nLower your body until your chest nearly touches the floor.' ||
  '\nPush back up to the starting position.',
  'push_up.png',
  FALSE,
  FALSE
);

INSERT INTO PUBLIC.EXERCISE VALUES (
  2,
  'Squat',
  'Stand with feet shoulder-width apart.' ||
  '\nLower your body by bending your knees until your thighs are parallel to the floor.' ||
  '\nPush back up to the starting position.',
  'squat.png',
  FALSE,
  TRUE
);

INSERT INTO PUBLIC.EXERCISE VALUES (
  3,
  'Plank',
  'Hold a push-up position with your arms straight.' ||
  '\nKeep your body in a straight line from head to heels.' ||
  '\nEngage your core and hold the position.',
  'plank.png',
  TRUE,
  FALSE
);

INSERT INTO PUBLIC.EXERCISE VALUES (
  4,
  'Pull-Up',
  'Grip a pull-up bar with palms facing away.' ||
  '\nPull yourself up until your chin clears the bar.' ||
  '\nLower yourself back down to the starting position.',
  'pull-up.png',
  FALSE,
  FALSE
);

INSERT INTO PUBLIC.EXERCISE VALUES (
  5,
  'Lunges',
  'Step forward with one leg.' ||
  '\nLower your hips until both knees are bent at 90-degree angles.' ||
  '\nPush back up to the starting position and repeat with the other leg.',
  'lunges.png',
  FALSE,
  FALSE
);


-- Insert sample tags
-- Muscle Groups
INSERT INTO PUBLIC.TAGS VALUES (1, 'Upper Body', 'MUSCLE_GROUP', '#1D4ED8', '#D1E8FF');
INSERT INTO PUBLIC.TAGS VALUES (2, 'Lower Body', 'MUSCLE_GROUP', '#6B21A8', '#E9D8FD');
INSERT INTO PUBLIC.TAGS VALUES (3, 'Core', 'MUSCLE_GROUP', '#0D9488', '#B2F5EA');
INSERT INTO PUBLIC.TAGS VALUES (4, 'Full Body', 'MUSCLE_GROUP', '#15803D', '#DCFCE7');

-- Difficulty Levels
INSERT INTO PUBLIC.TAGS VALUES (5, 'Beginner', 'DIFFICULTY', '#10B981', '#DCFCE7');
INSERT INTO PUBLIC.TAGS VALUES (6, 'Intermediate', 'DIFFICULTY', '#F59E0B', '#FEF3C7');
INSERT INTO PUBLIC.TAGS VALUES (7, 'Advanced', 'DIFFICULTY', '#DC2626', '#FEE2E2');

-- Exercise Types
INSERT INTO PUBLIC.TAGS VALUES (8, 'Cardio', 'EXERCISE_TYPE', '#D97706', '#FFEDD5');
INSERT INTO PUBLIC.TAGS VALUES (9, 'Strength', 'EXERCISE_TYPE', '#DB2777', '#FFD6E8');
INSERT INTO PUBLIC.TAGS VALUES (10, 'Flexibility', 'EXERCISE_TYPE', '#059669', '#D1FAE5');
INSERT INTO PUBLIC.TAGS VALUES (11, 'Balance', 'EXERCISE_TYPE', '#4338CA', '#E0E7FF');
INSERT INTO PUBLIC.TAGS VALUES (12, 'Endurance', 'EXERCISE_TYPE', '#DB2777', '#FFE4E6');

-- Equipment
INSERT INTO PUBLIC.TAGS VALUES (13, 'No Equipment', 'EQUIPMENT', '#1E3A8A', '#E0F2FE');
INSERT INTO PUBLIC.TAGS VALUES (14, 'Dumbbells', 'EQUIPMENT', '#BE185D', '#FCE7F3');
INSERT INTO PUBLIC.TAGS VALUES (15, 'Resistance Bands', 'EQUIPMENT', '#D97706', '#FEF3C7');


-- Insert links between exercises and tags
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (1, 1);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (1, 2);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (1, 3);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (1, 4);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (1, 15);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (2, 5);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (2, 7);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (2, 4);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (2, 6);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (2, 15);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (3, 4);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (3, 3);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (3, 8);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (3, 13);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (3, 14);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (4, 11);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (4, 15);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (4, 9);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (4, 10);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (5, 11);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (5, 15);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (5, 9);
INSERT INTO PUBLIC.EXERCISE_TAGS VALUES (5, 10);
