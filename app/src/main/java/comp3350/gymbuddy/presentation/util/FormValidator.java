package comp3350.gymbuddy.presentation.util;

import android.widget.EditText;

public class FormValidator {
    private boolean valid; // Whether the input so far has been valid.

    public FormValidator() {
        valid = true;
    }

    public int nextInt(EditText editText, int defaultValue) {
        int result = defaultValue;

        editText.setError(null);

        try {
            result = Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException e) {
            if (valid) {
                editText.setError("Required");
                valid = false;
            }
        }

        return result;
    }

    public double nextDouble(EditText editText, double defaultValue) {
        double result = defaultValue;

        editText.setError(null);

        try {
            result = Double.parseDouble(editText.getText().toString());
            editText.setError(null);
        } catch (NumberFormatException e) {
            if (valid) {
                editText.setError("Required");
                valid = false;
            }
        }

        return result;
    }

    public String nextString(EditText editText, String defaultValue) {
        String result = defaultValue;

        editText.setError(null);

        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            result = text;
        } else {
            if (valid) {
                editText.setError("Required");
                valid = false;
            }
        }

        return result;
    }

    public boolean getValid() {
        return valid;
    }
}
