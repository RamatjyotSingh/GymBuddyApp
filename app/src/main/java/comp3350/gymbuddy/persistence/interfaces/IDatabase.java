package comp3350.gymbuddy.persistence.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import comp3350.gymbuddy.persistence.exception.DBException;

/**
 * Interface defining the core database operations and lifecycle management
 */
public interface IDatabase {
    /**
     * Set up database with paths to script and configuration files
     * @param scriptPath Path to SQL script for initialization
     * @param configPath Path to configuration properties file
     */
    void setup(String scriptPath, String configPath);
    
    /**
     * Initialize the database schema and data
     * Should be called once during application startup
     */
    void initialize() throws DBException;
    
    /**
     * Check if database has been initialized
     * @return true if initialized, false otherwise
     */
    boolean isInitialized();
    
    /**
     * Get a connection to the database
     * @return Connection object
     * @throws DBException if connection cannot be established
     */
    Connection getConnection() throws DBException;
    
    /**
     * Close the current database connection
     * @throws DBException if connection cannot be closed
     */
    void closeConnection() throws DBException;
    
    /**
     * Close a specific database connection
     * @param connection The connection to close
     * @throws DBException if connection cannot be closed
     */
    void closeConnection(Connection connection) throws DBException;
    
    /**
     * Begin a database transaction
     * @return Connection with transaction started
     * @throws DBException if transaction cannot be started
     */
    Connection beginTransaction() throws DBException;
    
    /**
     * Commit a database transaction
     * @param connection Connection with active transaction
     * @throws DBException if transaction cannot be committed
     */
    void commitTransaction(Connection connection) throws DBException;
    
    /**
     * Rollback a database transaction
     * @param connection Connection with active transaction
     * @throws DBException if transaction cannot be rolled back
     */
    void rollbackTransaction(Connection connection) throws DBException;
    
    /**
     * Execute a SQL statement
     * @param sql SQL statement to execute
     * @throws DBException if statement execution fails
     */
    void executeStatement(String sql) throws DBException;
    
    /**
     * Execute a SQL script file
     * @param scriptPath Path to script file
     * @throws DBException if script execution fails
     */
    void executeScript(String scriptPath) throws DBException;
    
    /**
     * Perform database maintenance operations
     * (vacuum, reindex, integrity check, etc.)
     * @throws DBException if maintenance operations fail
     */
    void performMaintenance() throws DBException;
    
    /**
     * Get database configuration properties
     * @return Properties object containing database configuration
     */
    Properties getProperties();
    
    /**
     * Clean up database resources
     * Should be called during application shutdown
     */
    void cleanUp();
    
    /**
     * Shutdown the database
     * More forceful than cleanUp, ensures all resources are released
     * @throws DBException if shutdown fails
     */
    void shutdown() throws DBException;
    
    /**
     * Reset database to initial state
     * Typically used for testing
     * @throws DBException if reset fails
     */
    void reset() throws DBException;
}
