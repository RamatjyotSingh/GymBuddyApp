package comp3350.gymbuddy.logic.util;

/**
 * Configuration class for database settings
 */
public class ConfigLoader {
    private final String scriptPath;
    private final String configPath;
    private final boolean testMode;
    private final boolean dbAlreadyExists;

    /**
     * Creates a database configuration
     * @param scriptPath Path to database initialization script
     * @param configPath Path to database properties file
     * @param testMode Whether to use test data instead of real database
     * @param dbAlreadyExists Whether the database files already exist
     */
    private ConfigLoader(String scriptPath, String configPath, boolean testMode, boolean dbAlreadyExists) {
        this.scriptPath = scriptPath;
        this.configPath = configPath;
        this.testMode = testMode;
        this.dbAlreadyExists = dbAlreadyExists;
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public boolean isDbAlreadyExists() {
        return dbAlreadyExists;
    }

    /**
     * Builder for creating DatabaseConfig instances
     */
    public static class Builder {
        private String scriptPath;
        private String configPath;
        private boolean testMode = false;
        private boolean dbAlreadyExists = false;

        public Builder scriptPath(String scriptPath) {
            this.scriptPath = scriptPath;
            return this;
        }

        public Builder configPath(String configPath) {
            this.configPath = configPath;
            return this;
        }

        public Builder testMode(boolean testMode) {
            this.testMode = testMode;
            return this;
        }

        public Builder dbAlreadyExists(boolean dbAlreadyExists) {
            this.dbAlreadyExists = dbAlreadyExists;
            return this;
        }

        public ConfigLoader build() {
            if (scriptPath == null || scriptPath.isEmpty()) {
                throw new IllegalStateException("Script path must be provided");
            }
            if (configPath == null || configPath.isEmpty()) {
                throw new IllegalStateException("Config path must be provided");
            }
            return new ConfigLoader(scriptPath, configPath, testMode, dbAlreadyExists);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}