package comp3350.gymbuddy;

import android.app.Application;
import android.content.Context;

/**
 * This was created as a workaround for initializing the database with tables and data.
 *
 * We are aware that this isn't the proper way to do this, and have created an issue on GitLab
 * to correct this in iteration 3.
 */
public class GlobalApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
