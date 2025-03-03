package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.objects.Tag.TagType;
import comp3350.gymbuddy.persistence.interfaces.ITagPersistence;

public class TagHSQLDB implements ITagPersistence {
    private final Connection connection;

    public TagHSQLDB(final Connection connection) {
        this.connection = connection;
    }

    public Tag getTagByName(String name) {
        String query = "SELECT TAG_TYPE, TEXT_COLOR, BG_COLOR FROM TAGS WHERE TAG_NAME = ?";
        Tag tag=null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String typeStr = rs.getString("TAG_TYPE");
                    String textColor = rs.getString("TEXT_COLOR");
                    String bgColor = rs.getString("BG_COLOR");

                    TagType type = TagType.valueOf(typeStr); // Convert string to enum
                    tag= new Tag(type, name, textColor, bgColor);
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
        return tag; // Return null if no tag is found
    }

    @Override
    public List<Tag> getAll() {
        List<Tag> tagList = new ArrayList<>();
        String query = "SELECT TAG_NAME, TAG_TYPE, TEXT_COLOR, BG_COLOR FROM TAGS";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String name = rs.getString("TAG_NAME");
                String typeStr = rs.getString("TAG_TYPE");
                String textColor = rs.getString("TEXT_COLOR");
                String bgColor = rs.getString("BG_COLOR");

                TagType type = TagType.valueOf(typeStr);
                tagList.add(new Tag(type, name, textColor, bgColor));
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

        return tagList;
    }

    @Override
    public List<Tag> getTagsByExerciseID(int exerciseID) {
        List<Tag> tagList = new ArrayList<>();
        String query = "SELECT T.TAG_NAME, T.TAG_TYPE, T.TEXT_COLOR, T.BG_COLOR " +
                "FROM TAGS T " +
                "JOIN EXERCISE_TAGS lt ON T.TAG_ID = lt.TAG_ID " +
                "WHERE lt.EXERCISE_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, exerciseID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("TAG_NAME");
                    String typeStr = rs.getString("TAG_TYPE");
                    String textColor = rs.getString("TEXT_COLOR");
                    String bgColor = rs.getString("BG_COLOR");

                    TagType type = TagType.valueOf(typeStr);
                    tagList.add(new Tag(type, name, textColor, bgColor));
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }

        Collections.shuffle(tagList);  // Shuffle the list to randomize
        return tagList.size() > 5 ? tagList.subList(0, 5) : tagList;  // Return up to 5 tags
    }
}
