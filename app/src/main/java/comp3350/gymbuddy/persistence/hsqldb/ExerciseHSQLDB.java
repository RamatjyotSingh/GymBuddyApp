package comp3350.gymbuddy.persistence.hsqldb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import comp3350.gymbuddy.objects.Exercise;
import comp3350.gymbuddy.objects.Tag;
import comp3350.gymbuddy.persistence.interfaces.IExercisePersistence;

public class ExerciseHSQLDB implements IExercisePersistence {

    private final Connection connection;

    public ExerciseHSQLDB(final Connection connection) {
        this.connection= connection;
    }

    private List<Tag> getTagsForExercise(int exerciseID) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT t.TAG_NAME, t.TAG_TYPE, t.TEXT_COLOR, t.BG_COLOR " +
                "FROM LINKS_TO lt " +
                "JOIN TAGS t ON lt.TAG_ID = t.TAG_ID " +
                "WHERE lt.EXERCISE_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, exerciseID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("TAG_NAME");
                    String typeStr = rs.getString("TAG_TYPE");
                    String textColor = rs.getString("TEXT_COLOR");
                    String bgColor = rs.getString("BG_COLOR");
                    Tag.TagType type = Tag.TagType.valueOf(typeStr);

                    tags.add(new Tag(type, name,textColor,bgColor));
                }
            }
        }
        return tags;
    }

    private ArrayList<String> getInstructionsForExercise(int exerciseID) throws SQLException {
        ArrayList<String> instructions = new ArrayList<>();
        String query = "SELECT TEXT FROM INSTRUCTION WHERE EXERCISE_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, exerciseID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    instructions.add(rs.getString("TEXT"));
                }
            }
        }
        return instructions;
    }

    @Override
    public List<Exercise> getAll() {
        List<Exercise> exercises = new ArrayList<>();
        String query = "SELECT * FROM EXERCISE";

            try( Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int exerciseID = rs.getInt("EXERCISE_ID");
                String name = rs.getString("NAME");
                String imagePath= rs.getNString("IMAGE_PATH");

                // Fetch related tags and instructions
                List<Tag> tags = getTagsForExercise(exerciseID);
                ArrayList<String> instructions = getInstructionsForExercise(exerciseID);

                exercises.add(new Exercise(exerciseID, name, tags, instructions,imagePath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    @Override
    public Exercise getExerciseByName(String name) {
        String query = "SELECT * FROM EXERCISE WHERE NAME = ?";


             try(PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int exerciseID = rs.getInt("EXERCISE_ID");
                    String imagePath= rs.getNString("IMAGE_PATH");

                    // Fetch related tags and instructions
                    List<Tag> tags = getTagsForExercise(exerciseID);
                    ArrayList<String> instructions = getInstructionsForExercise(exerciseID);



                    return new Exercise(exerciseID, name, tags, instructions,imagePath);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Exercise getExerciseByID(int id) {
        String query = "SELECT * FROM EXERCISE WHERE EXERCISE_ID = ?";


        try(PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("NAME");
                    String imagePath= rs.getNString("IMAGE_PATH");

                    // Fetch related tags and instructions
                    List<Tag> tags = getTagsForExercise(id);
                    ArrayList<String> instructions = getInstructionsForExercise(id);

                    return new Exercise(id, name, tags, instructions,imagePath);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
