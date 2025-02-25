package comp3350.gymbuddy.persistence.dao;

import android.content.Context;
import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp3350.gymbuddy.application.ConfigManager;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.database.DatabaseManager;
import comp3350.gymbuddy.persistence.interfaces.ITagPersistence;

public class TagDao implements ITagPersistence {

    private final Connection connection;
    private final ConfigManager queryConfig;
    private static final String TAG = "TagDao";

    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TEXT_COLOR = "textColor";
    public static final String COLUMN_BG_COLOR = "bgColor";



    // Constructor to initialize database connection and query configuration
    public TagDao(Context context) {
        this.connection = DatabaseManager.getInstance(context).getConnection();
        this.queryConfig = new ConfigManager(context, "sql_queries.properties");
    }

    // Method to retrieve all tags from the database
    @Override
    public List<Tag> getAll() {
        List<Tag> tags = new ArrayList<>();
        String query = queryConfig.getProperty("tag.get_all");

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tag.TagType type = Tag.TagType.valueOf(rs.getString(COLUMN_TYPE));
                String name = rs.getString(COLUMN_NAME);
                String textColor = rs.getString(COLUMN_TEXT_COLOR);
                String bgColor = rs.getString(COLUMN_BG_COLOR);

                tags.add(new Tag(type, name, textColor, bgColor));
            }
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return tags;
    }

    // Method to retrieve tags by exercise ID from the database
    @Override
    public List<Tag> getTagsByExerciseID(int exerciseID) {
        List<Tag> tags = new ArrayList<>();
        String query = queryConfig.getProperty("tag.get_by_exercise_id");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, exerciseID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tag.TagType type = Tag.TagType.valueOf(rs.getString("type"));
                    String name = rs.getString("name");
                    String textColor = rs.getString("textColor");
                    String bgColor = rs.getString("bgColor");

                    tags.add(new Tag(type, name, textColor, bgColor));
                }
            }
        } catch (SQLException e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return tags;
    }

    // Method to close the database connection
    public void close() {
        DatabaseManager.getInstance(null).closeConnection();
    }
}