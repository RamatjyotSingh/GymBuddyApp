package comp3350.gymbuddy.tests.integration;

import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import comp3350.gymbuddy.persistence.exception.DBException;
import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;
import timber.log.Timber;

/**
 * Test class to verify the test database is set up correctly.
 */
public class DatabaseSetupTest extends DBIntegrationTestHelper {
    private static final String TAG = "DBSetupTest";


    
    @Test
    public void testDatabaseConnection() {
        try (Connection conn = HSQLDBHelper.getConnection()) {
            assertNotNull("Database connection should not be null", conn);
            assertFalse("Database connection should not be closed", conn.isClosed());
            
            // Check if we're connected to the test database
            String url = conn.getMetaData().getURL();
            Timber.tag(TAG).d("Connected to database: %s", url);
            
            assertTrue("Should be connected to test database", 
                    url.contains("test_") || url.contains("test_gymbuddydb"));
        } catch (SQLException | DBException e) {
            fail("Failed to connect to database: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseTables() {
        // Define expected tables based on Project.script
        List<String> expectedTables = new ArrayList<>();
        expectedTables.add("EXERCISE");
        expectedTables.add("TAG");
        expectedTables.add("EXERCISE_TAG");
        expectedTables.add("WORKOUT_PROFILE");
        expectedTables.add("PROFILE_EXERCISE");
        expectedTables.add("WORKOUT_SESSION");
        expectedTables.add("SESSION_ITEM");  // Added missing SESSION_ITEM table
        
        try (Connection conn = HSQLDBHelper.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            // Check each expected table
            for (String tableName : expectedTables) {
                boolean tableExists = false;
                
                try (ResultSet tables = metaData.getTables(null, "PUBLIC", tableName, null)) {
                    tableExists = tables.next();
                }
                
                assertTrue("Table " + tableName + " should exist in database", tableExists);
            }
            
            // Also log tables for debugging
            Timber.tag(TAG).d("Found database tables:");
            try (ResultSet allTables = metaData.getTables(null, "PUBLIC", "%", null)) {
                while (allTables.next()) {
                    Timber.tag(TAG).d(" - %s", allTables.getString("TABLE_NAME"));
                }
            }
        } catch (SQLException | DBException e) {
            fail("Failed to check database tables: " + e.getMessage());
        }
    }
    
    @Test
    public void testInitialData() {
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check for each essential table
            String[] tables = {"EXERCISE", "TAG", "EXERCISE_TAG", "WORKOUT_PROFILE"};
            
            for (String table : tables) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    assertTrue("Should have result for count query on " + table, rs.next());
                    int count = rs.getInt(1);
                    assertTrue("Should have data in table " + table, count > 0);
                    Timber.tag(TAG).d("Found %d rows in %s table", count, table);
                }
            }
            
            // Check for specific known data (Push-Up exercise)
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM EXERCISE WHERE NAME = 'Push-Up'")) {
                assertTrue("Should have result for Push-Up query", rs.next());
                int count = rs.getInt(1);
                assertEquals("Should find exactly one Push-Up exercise", 1, count);
            }
        } catch (SQLException | DBException e) {
            fail("Failed to check initial data: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseModification() {
        // Verify we can modify the database
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            // Get next available exercise_id
            int nextId;
            try (ResultSet rs = stmt.executeQuery("SELECT MAX(exercise_id) + 1 FROM EXERCISE")) {
                rs.next();
                nextId = rs.getInt(1);
                if (nextId <= 0) nextId = 100; // Fallback if no exercises or error
            }

            // Insert a test record using proper schema
            String testName = "Test Exercise " + System.currentTimeMillis();
            String insertSql = String.format(
                "INSERT INTO EXERCISE (exercise_id, name, instructions, is_time_based, has_weight) " +
                "VALUES (%d, '%s', 'Test instructions', false, false)",
                nextId, testName
            );

            int rowsAffected = stmt.executeUpdate(insertSql);
            assertEquals("Should insert one row", 1, rowsAffected);

            // Verify it was inserted
            try (ResultSet rs = stmt.executeQuery("SELECT NAME FROM EXERCISE WHERE NAME = '" + testName + "'")) {
                assertTrue("Should find inserted exercise", rs.next());
                assertEquals("Exercise name should match", testName, rs.getString("NAME"));
                assertFalse("Should only have one result", rs.next());
            }

            Timber.tag(TAG).d("Successfully inserted and retrieved test data");

            // Now test modifying the row
            String updateSql = String.format(
                "UPDATE EXERCISE SET instructions = 'Modified instructions' WHERE exercise_id = %d",
                nextId
            );

            rowsAffected = stmt.executeUpdate(updateSql);
            assertEquals("Should update one row", 1, rowsAffected);

            // Verify update
            try (ResultSet rs = stmt.executeQuery("SELECT instructions FROM EXERCISE WHERE exercise_id = " + nextId)) {
                assertTrue("Should find updated exercise", rs.next());
                assertEquals("Instructions should be updated", "Modified instructions", rs.getString("instructions"));
            }

            Timber.tag(TAG).d("Successfully updated test data");
        } catch (SQLException | DBException e) {
            fail("Failed to modify database: " + e.getMessage());
        }
    }

    @Test
    public void testTableRelationships() {
        try (Connection conn = HSQLDBHelper.getConnection();
             Statement stmt = conn.createStatement()) {

            // Test exercise-tag relationship
            try (ResultSet rs = stmt.executeQuery(
                "SELECT e.name, t.tag_name FROM EXERCISE e " +
                "JOIN EXERCISE_TAG et ON e.exercise_id = et.exercise_id " +
                "JOIN TAG t ON et.tag_id = t.tag_id " +
                "WHERE e.name = 'Push-Up'"
            )) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    String exerciseName = rs.getString("name");
                    String tagName = rs.getString("tag_name");
                    Timber.tag(TAG).d("Exercise '%s' has tag '%s'", exerciseName, tagName);
                    assertEquals("Exercise name should be Push-Up", "Push-Up", exerciseName);
                }

                assertTrue("Push-Up should have at least one tag", count > 0);
            }

            // Test workout profile relationship
            try (ResultSet rs = stmt.executeQuery(
                "SELECT wp.profile_name, e.name FROM WORKOUT_PROFILE wp " +
                "JOIN PROFILE_EXERCISE pe ON wp.profile_id = pe.profile_id " +
                "JOIN EXERCISE e ON pe.exercise_id = e.exercise_id " +
                "WHERE wp.profile_name = 'Full Body Workout'"
            )) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    String profileName = rs.getString("profile_name");
                    String exerciseName = rs.getString("name");
                    Timber.tag(TAG).d("Workout '%s' includes exercise '%s'", profileName, exerciseName);
                    assertEquals("Profile name should be Full Body Workout", "Full Body Workout", profileName);
                }

                assertTrue("Full Body Workout should have at least one exercise", count > 0);
            }
        } catch (SQLException | DBException e) {
            fail("Failed to test relationships: " + e.getMessage());
        }
    }
}