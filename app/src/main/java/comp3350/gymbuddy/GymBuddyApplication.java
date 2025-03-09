package comp3350.gymbuddy;

import android.app.Application;
import android.content.Context;
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

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        
        // Extract SQL files to internal storage once
        extractSqlFilesToStorage();
    }

    public static Context getAppContext() {
        return appContext;
    }

    private void extractSqlFilesToStorage() {
        String[] sqlFiles = {"create_tables.sql", "insert_data.sql"};
        File sqlDir = new File(getFilesDir(), "sql_scripts");
        
        if (!sqlDir.exists()) {
            sqlDir.mkdirs();
        }
        
        for (String filename : sqlFiles) {
            try (InputStream is = getAssets().open(filename);
                 FileOutputStream fos = new FileOutputStream(new File(sqlDir, filename))) {
                
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                
                Timber.tag("GlobalApp").d("Extracted SQL file: %s", filename);
            } catch (IOException e) {
                Timber.tag("GlobalApp").e("Failed to extract SQL file: %s", filename);
            }
        }
        
        // Initialize database with paths
        HSQLDBHelper.setDatabaseDirectoryPath(getFilesDir().getAbsolutePath());
        HSQLDBHelper.setSqlScriptDirectory(new File(sqlDir.getAbsolutePath()).getAbsolutePath());
    }
}
