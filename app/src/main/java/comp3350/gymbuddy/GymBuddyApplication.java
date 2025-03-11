package comp3350.gymbuddy;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import comp3350.gymbuddy.persistence.hsqldb.HSQLDBHelper;
import timber.log.Timber;

/**
 * This was created as a workaround for initializing the database with tables and data.
 *
 * We are aware that this isn't the proper way to do this, and have created an issue on GitLab
 * to correct this in iteration 3.
 */
public class GymBuddyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        initializeDatabase();
    }

    /**
     * Initializes the database, avoiding reinitialization if it already exists
     */
    private void initializeDatabase() {
        // Create DB directory
        File dbDir = new File(getFilesDir(), "db");
        if (!dbDir.exists() && !dbDir.mkdirs()) {
            Timber.tag("GlobalApp").e("Failed to create DB directory");
            return;
        }
        

        File dbFile = new File(dbDir, "gymbuddydb.script");

        if(dbFile.exists() && dbFile.length() > 0) {
            Timber.tag("GlobalApp").d("Database already exists");
            HSQLDBHelper.setDatabaseDirectoryPath(dbDir.getAbsolutePath());
            return;
        }

        Timber.tag("GlobalApp").d("Database needs to be initialized");
        
        // Extract required files
        String[] dbFiles = {"db/Project.script", "db/DBConfig.properties"};
        for (String filepath : dbFiles) {
            String filename = new File(filepath).getName();
            extractFile(filepath, dbDir, filename);
        }
        
        // Set path and initialize database
        HSQLDBHelper.setDatabaseDirectoryPath(dbDir.getAbsolutePath());
        try {
            HSQLDBHelper.init();
            Timber.tag("GlobalApp").d("Database initialized successfully");
        } catch (Exception e) {
            Timber.tag("GlobalApp").e("Failed to initialize database: %s", e.getMessage());
        }
    }

    /**
     * Extract a specific file from assets to the destination directory
     * 
     * @param assetPath Source path in assets
     * @param destDir Destination directory 
     * @param destFilename Destination filename
     */
    private void extractFile(String assetPath, File destDir, String destFilename) {
        File outputFile = new File(destDir, destFilename);
        
        // Skip if file already exists and is not empty
        if (outputFile.exists() && outputFile.length() > 0) {
            Timber.tag("GlobalApp").d("File already exists: %s", destFilename);
            return;
        }
        
        try (InputStream is = getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[8192]; // 8KB buffer for better performance
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            Timber.tag("GlobalApp").d("Extracted file: %s → %s", assetPath, outputFile.getAbsolutePath());
        } catch (IOException e) {
            Timber.tag("GlobalApp").e("Failed to extract file: %s - %s", assetPath, e.getMessage());
        }
    }
}
