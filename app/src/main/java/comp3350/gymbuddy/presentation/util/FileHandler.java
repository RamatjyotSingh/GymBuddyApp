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
 * Utility class for extracting files from assets to the app's internal storage.
 */
public class FileHandler {
    private static final String TAG = "FileHandler";

    /**
     * Extract multiple asset files to a specified directory
     *
     * @param context         Application context
     * @param assetPaths      Array of asset paths to extract
     * @param relativeDirPath Relative directory path within app files
     * @return Map of filenames to absolute paths, or null if extraction failed
     */
    public static Map<String, String> extractAssetFiles(Context context, String[] assetPaths,
                                                        String relativeDirPath, ErrorHandler handler)  {
       try {
            File destDir = ensureDirectoryExists(context, relativeDirPath);
            Map<String, String> paths = new HashMap<>();
            
            for (String assetPath : assetPaths) {
               
                    File extractedFile = extractAssetToFile(context, assetPath, destDir);
                    String filename = extractedFile.getName();
                    paths.put(filename, extractedFile.getAbsolutePath());
                 
            }
            
            // Include directory path for convenience
            paths.put("directory", destDir.getAbsolutePath());
            return paths;
        } catch (IOException e) {
            handler.handle(e, handler.getDefaultErrorMessage());
            return null;
       }
       
    }

    /**
     * Extract an asset file to the specified directory
     *
     * @param context   Application context
     * @param assetPath Source path in assets
     * @param destDir   Destination directory
     * @return The extracted file
     * @throws IOException If extraction fails
     */
    public static File extractAssetToFile(Context context, String assetPath, File destDir) throws IOException {
        String filename = new File(assetPath).getName();
        File outputFile = new File(destDir, filename);
        
        // Simplified file existence check
        if (outputFile.exists() && !outputFile.delete()) {
            Timber.tag(TAG).w("Failed to delete existing file: %s, returning old one", outputFile.getAbsolutePath());
            return outputFile;
        }

        try (InputStream is = context.getAssets().open(assetPath);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            return outputFile;
        }
    }
    
    /**
     * Creates a directory if it doesn't exist
     *
     * @param context      Application context
     * @param relativePath Relative path within app's files directory
     * @return The directory File object
     * @throws IOException If directory creation fails
     */
    public static File ensureDirectoryExists(Context context, String relativePath) throws IOException {

        File dir = new File(context.getFilesDir(), relativePath);
        if (!dir.exists() && !dir.mkdirs()) {
           throw new IOException("Failed to create directory");
        }
        return dir;
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
}
