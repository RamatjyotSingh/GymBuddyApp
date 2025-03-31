package comp3350.gymbuddy.presentation.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Implementation of ErrorDisplay that shows errors using Android Toast
 */
public class ToastErrorDisplay implements ErrorDisplay {
    private final Context context;
    
    public ToastErrorDisplay(Context context) {
        this.context = context;
    }
    
    @Override
    public void showError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}