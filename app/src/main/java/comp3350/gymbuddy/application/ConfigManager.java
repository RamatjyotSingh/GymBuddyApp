package comp3350.gymbuddy.application;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String TAG = "ConfigManager";
    private final Properties properties = new Properties();

    public ConfigManager(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (Exception e) {
            Log.e(TAG, "Error loading config: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getActiveDbProperty(String key) {
        String activeDb = getProperty("db.active");  // "mem" or "file"
        return getProperty("db." + activeDb + "." + key);
    }
}
