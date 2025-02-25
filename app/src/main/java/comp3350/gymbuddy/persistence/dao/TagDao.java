package comp3350.gymbuddy.persistence.dao;


import android.util.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.ITagPersistence;

public class TagDao implements ITagPersistence {
    private Connection connection;
    private static final String TAG = "TagDao";  // Or "MainActivity", "MyActivity", etc.

    // Constructor to initialize the connection
    public TagDao() {
        try {
            // Assuming the HSQLDB database is running locally on the default port and db name is "mydb"
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/mydb", "SA", "");
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }
@Override
    // Method to fetch all tags from the database
    public List<Tag> getAll() {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT * FROM tags";  // Query to fetch all tags

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Get values from the result set and map to Tag object
                Tag.TagType type = Tag.TagType.valueOf(rs.getString("type"));  // Assuming type is stored as a string like "DIFFICULTY"
                String name = rs.getString("name");
                String textColor = rs.getString("textColor");
                String bgColor = rs.getString("bgColor");

                // Create a new Tag object
                Tag tag = new Tag(type, name, textColor, bgColor);

                // Add the tag to the list
                tags.add(tag);
            }
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return tags;  // Return the list of tags
    }

@Override
public List<Tag> getTagsByExerciseID(int ExerciseID) {
    List<Tag> tags = new ArrayList<>();
    String query = "SELECT * FROM tags JOIN exercise_tags ON tags.tagID = exercise_tags.tagID WHERE exercise_tags.exerciseID = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, ExerciseID);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tag.TagType type = Tag.TagType.valueOf(rs.getString("type"));
                String name = rs.getString("name");
                String textColor = rs.getString("textColor");
                String bgColor = rs.getString("bgColor");

                Tag tag = new Tag(type, name, textColor, bgColor);
                tags.add(tag);
            }
        }
    } catch (SQLException e) {
        Log.d(TAG, Objects.requireNonNull(e.getMessage()));
    }

    return tags;
}


    // Close the database connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }


}
