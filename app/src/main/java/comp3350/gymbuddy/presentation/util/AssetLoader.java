package comp3350.gymbuddy.presentation.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class AssetLoader {

    /**
     * Loads an image from the assets directory.
     *
     * @param context Application context
     * @param filepath path to the image relative to assets/
     * @return the image or null if not found
     */
    public Bitmap loadImage(Context context, String filepath) {
        Bitmap result = null;

        try (InputStream inputStream = context.getAssets().open(filepath)) {
            result = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            ErrorHandler handler = new ErrorHandler(new ToastErrorDisplay(context));
            handler.handle(e);
        }

        return result;
    }
}
