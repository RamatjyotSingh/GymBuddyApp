-- Exercise table stores exercises with metadata
CREATE TABLE IF NOT EXISTS PUBLIC.exercise (
    exercise_id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    instructions VARCHAR(1000) NOT NULL,
    image_path VARCHAR(255),
    is_time_based BOOLEAN NOT NULL,
    has_weight BOOLEAN NOT NULL
);

-- Tags table for categorizing exercises with colors for UI display
CREATE TABLE IF NOT EXISTS PUBLIC.tag (
    tag_id INTEGER NOT NULL PRIMARY KEY,
    tag_name VARCHAR(50) NOT NULL,
    tag_type INTEGER NOT NULL,
    text_color VARCHAR(7) NOT NULL,
    background_color VARCHAR(7) NOT NULL
);

-- Many-to-many relationship between exercises and tags
CREATE TABLE IF NOT EXISTS PUBLIC.exercise_tag (
    exercise_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    CONSTRAINT pk_exercise_tag PRIMARY KEY (exercise_id, tag_id),
    CONSTRAINT fk_exercise_tag_exercise FOREIGN KEY (exercise_id)
        REFERENCES PUBLIC.exercise (exercise_id) ON DELETE CASCADE,
    CONSTRAINT fk_exercise_tag_tag FOREIGN KEY (tag_id)
        REFERENCES PUBLIC.tag (tag_id) ON DELETE CASCADE
);

-- Workout profiles representing different user-defined workouts
CREATE TABLE IF NOT EXISTS PUBLIC.workout_profile (
    profile_id INTEGER PRIMARY KEY,
    profile_name VARCHAR(255) NOT NULL,
    icon_path VARCHAR(255) DEFAULT NULL
);

-- Store exercises assigned to workout profiles
CREATE TABLE IF NOT EXISTS PUBLIC.profile_exercise (
    profile_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    weight DOUBLE PRECISION DEFAULT 0,
    duration DOUBLE PRECISION DEFAULT 0,
    CONSTRAINT pk_profile_exercise PRIMARY KEY (profile_id, exercise_id),
    CONSTRAINT fk_profile_exercise_profile FOREIGN KEY (profile_id)
        REFERENCES PUBLIC.workout_profile (profile_id) ON DELETE CASCADE,
    CONSTRAINT fk_profile_exercise_exercise FOREIGN KEY (exercise_id)
        REFERENCES PUBLIC.exercise (exercise_id) ON DELETE CASCADE
);

-- Workout session logs past workouts with timestamps
CREATE TABLE IF NOT EXISTS PUBLIC.workout_session (
    session_id INTEGER PRIMARY KEY,
    start_time BIGINT NOT NULL,
    end_time BIGINT NOT NULL,
    profile_id INTEGER,
    CONSTRAINT fk_workout_session_profile FOREIGN KEY (profile_id)
        REFERENCES PUBLIC.workout_profile (profile_id) ON DELETE SET NULL
);

-- Session items track individual exercises performed in a session
CREATE TABLE IF NOT EXISTS PUBLIC.session_item (
    session_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    reps INTEGER DEFAULT 0,
    weight DOUBLE PRECISION DEFAULT 0,
    duration INTEGER DEFAULT 0,
    CONSTRAINT pk_session_item PRIMARY KEY (session_id, exercise_id),
    CONSTRAINT fk_session_item_session FOREIGN KEY (session_id)
        REFERENCES PUBLIC.workout_session (session_id) ON DELETE CASCADE,
    CONSTRAINT fk_session_item_exercise FOREIGN KEY (exercise_id)
        REFERENCES PUBLIC.exercise (exercise_id) ON DELETE CASCADE
);