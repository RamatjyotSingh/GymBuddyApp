package comp3350.gymbuddy.presentation.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Utility class for extracting files from assets to the app's internal storage
 */
public class FileExtractor {
    private static final String TAG = "FileExtractor";


    /**
     * Extract multiple asset files to a specified directory
     *
     * @param context Application context
     * @param assetPaths Array of asset paths to extract
     * @param relativeDirPath Relative directory path within app files
     * @param errorDisplay Interface to display errors
     * @return Map of filenames to absolute paths, or null if extraction failed
     */
    public static Map<String, String> extractAssetFiles(Context context, String[] assetPaths, 
                                                     String relativeDirPath, ErrorDisplay errorDisplay) {
        File destDir = ensureDirectoryExists(context, relativeDirPath, errorDisplay);
        if (destDir == null) {
            return null;
        }

        Map<String, String> paths = new HashMap<>();
        
        for (String assetPath : assetPaths) {
            File extractedFile = extractAssetToFile(context, assetPath, destDir, errorDisplay);
            if (extractedFile != null) {
                String filename = extractedFile.getName();
                paths.put(filename, extractedFile.getAbsolutePath());
                Timber.tag(TAG).d("Extracted file: %s → %s", filename, extractedFile.getAbsolutePath());
            } else {
                return null; // If any extraction fails, return null
            }
        }
        
        // Include directory path for convenience
        paths.put("directory", destDir.getAbsolutePath());
        return paths;
    }

    /**
     * Extract an asset file to the specified directory
     *
     * @param context Application context
     * @param assetPath Source path in assets
     * @param destDir Destination directory
     * @param errorDisplay Interface to display errors
     * @return The extracted file or null if extraction failed
     */
    public static File extractAssetToFile(Context context, String assetPath, File destDir, ErrorDisplay errorDisplay) {
        String filename = new File(assetPath).getName();
        File outputFile = new File(destDir, filename);
        Timber.tag(TAG).d("Extracting file: %s → %s", assetPath, outputFile.getAbsolutePath());

        // Skip if file already exists and is not empty
        if (outputFile.exists() && outputFile.length() > 0) {
            Timber.tag(TAG).d("File already exists: %s", filename);
            return outputFile;
        }

        try (InputStream is = context.getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8192]; // 8KB buffer for better performance
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            Timber.tag(TAG).d("Extracted file: %s → %s", assetPath, outputFile.getAbsolutePath());
            return outputFile;
        } catch (IOException e) {
            errorDisplay.showError(context.getString(comp3350.gymbuddy.R.string.internal_error));
            Timber.tag(TAG).e("Failed to extract file: %s - %s", assetPath, e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a directory if it doesn't exist
     * 
     * @param context Application context
     * @param relativePath Relative path within app's files directory
     * @param errorDisplay Interface to display errors
     * @return The directory File object, or null if creation failed
     */
    public static File ensureDirectoryExists(Context context, String relativePath, ErrorDisplay errorDisplay) {
        File dir = new File(context.getFilesDir(), relativePath);
        if (!dir.exists() && !dir.mkdirs()) {
            errorDisplay.showError(context.getString(comp3350.gymbuddy.R.string.internal_error));
            Timber.tag(TAG).e("Failed to create directory: %s", relativePath);
            return null;
        }
        return dir;
    }
    
    /**
     * Extracts a single file from assets to the app's files directory and returns its path
     * 
     * @param context Application context
     * @param assetPath Source path in assets
     * @param relativeDirPath Relative directory path within app files
     * @param errorDisplay Interface to display errors
     * @return Absolute path to the extracted file, or null if extraction failed
     */
    public static String extractSingleFile(Context context, String assetPath, String relativeDirPath,
                                         ErrorDisplay errorDisplay) {
        File destDir = ensureDirectoryExists(context, relativeDirPath, errorDisplay);
        if (destDir == null) {
            return null;
        }
        
        File extractedFile = extractAssetToFile(context, assetPath, destDir, errorDisplay);
        return extractedFile != null ? extractedFile.getAbsolutePath() : null;
    }
    
    /**
     * Checks if a file exists in the app's internal storage
     *
     * @param context Application context
     * @param relativePath Path relative to the app's files directory
     * @param filename Name of the file to check
     * @return true if the file exists and has content
     */
    public static boolean fileExists(Context context, String relativePath, String filename) {
        File file = new File(new File(context.getFilesDir(), relativePath), filename);
        return file.exists() && file.length() > 0;
    }
    
    /**
     * Gets the absolute path for a file in the app's internal storage
     * 
     * @param context Application context
     * @param relativePath Path relative to the app's files directory
     * @param filename Name of the file
     * @return Absolute path to the file
     */
    public static String getFilePath(Context context, String relativePath, String filename) {
        File file = new File(new File(context.getFilesDir(), relativePath), filename);
        return file.getAbsolutePath();
    }
    
    /**
     * Checks if database files already exist
     * @param context Application context
     * @return true if database files exist, false otherwise
     */
    public static boolean databaseExists(Context context) {
        String dbDirPath = context.getString(comp3350.gymbuddy.R.string.db_dir);
        File dbDir = new File(context.getFilesDir(), dbDirPath);
        
        if (!dbDir.exists()) {
            return false;
        }
        
        // Check for essential database files
        File scriptFile = new File(dbDir, "Project.script");
        File propertiesFile = new File(dbDir, "DBConfig.properties");
        
        return scriptFile.exists() && scriptFile.length() > 0 
               && propertiesFile.exists() && propertiesFile.length() > 0;
    }
}
