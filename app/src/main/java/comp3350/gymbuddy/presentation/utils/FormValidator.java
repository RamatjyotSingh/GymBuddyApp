package comp3350.gymbuddy.presentation.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for validating form input from EditText fields in an Android Activity.
 * <p>
 * Usage example:
 * <pre>
 *   FormValidator formValidator = new FormValidator(this);
 *
 *   formValidator.addEditText(R.id.edtSets).validInt().greaterThan(0);
 *   formValidator.addEditText(R.id.edtReps).validInt().greaterThan(0);
 *   formValidator.addEditText(R.id.edtTime).validDouble().greaterThan(0.0);
 *   formValidator.addEditText(R.id.edtWeight).validDouble();
 *   formValidator.addEditText(R.id.edtName).notEmpty();
 *
 *   if (formValidator.validateAll()) {
 *       int sets = formValidator.getInt(R.id.edtSets);
 *       int reps = formValidator.getInt(R.id.edtReps);
 *       double time = formValidator.getDouble(R.id.edtTime);
 *       double weight = formValidator.getTime(R.id.edtWeight);
 *       String name = formValidator.getString(R.id.edtName);
 *   }
 * </pre>
 * </p>
 */
public class FormValidator {

    private final View rootView; // Top-level view to search.

    // Map of resource IDs to validators. LinkedHashMap preserves insertion order.
    private final Map<Integer, EditTextValidator> validators = new LinkedHashMap<>();

    /**
     * Constructs a FormValidator using the specified view to locate EditText fields.
     *
     * @param rootView the view containing the EditText fields.
     */
    public FormValidator(View rootView) {
        this.rootView = rootView;
    }

    /**
     * Constructs a FormValidator using the specified Activity to locate EditText fields.
     *
     * @param activity the activity containing the EditText fields.
     */
    public FormValidator(Activity activity) {
        this.rootView = activity.findViewById(android.R.id.content);
    }

    /**
     * Adds an EditText to be validated.
     *
     * @param viewId the resource ID of the EditText.
     * @return a EditTextValidator for chaining validation constraints.
     */
    public EditTextValidator addEditText(int viewId) {
        EditText editText = rootView.findViewById(viewId);
        EditTextValidator validator = new EditTextValidator(editText);
        validators.put(viewId, validator);
        return validator;
    }

    /**
     * Validates all added EditText fields in the order they were added.
     * Only the first error encountered is shown.
     *
     * @return true if all validations pass; false otherwise.
     */
    public boolean validateAll() {
        // Clear all errors.
        for (EditTextValidator validator : validators.values()) {
            validator.getEditText().setError(null);
        }

        for (EditTextValidator validator : validators.values()) {
            // Validate each field.
            if (!validator.validate()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the integer value from the specified EditText.
     *
     * @param viewId the resource ID of the EditText.
     * @return the parsed integer value.
     * @throws NumberFormatException if the text is not a valid integer.
     */
    public int getInt(int viewId) {
        return validators.get(viewId).getInt();
    }

    /**
     * Retrieves the double value from the specified EditText.
     *
     * @param viewId the resource ID of the EditText.
     * @return the parsed double value.
     * @throws NumberFormatException if the text is not a valid double.
     */
    public double getDouble(int viewId) {
        return validators.get(viewId).getDouble();
    }

    /**
     * Retrieves the text from the specified EditText.
     *
     * @param viewId the resource ID of the EditText.
     * @return the text string.
     */
    public String getString(int viewId) {
        return validators.get(viewId).getString();
    }

    /**
     * Enum representing the expected type of input for an EditText field.
     */
    private enum ExpectedType {
        NONE,
        INT,
        DOUBLE,
        STRING
    }

    /**
     * Interface for defining a validation constraint on a text input.
     */
    private interface Constraint {
        /**
         * Checks whether the provided text satisfies this constraint.
         *
         * @param text the text to validate.
         * @return true if the constraint is satisfied; false otherwise.
         */
        boolean isValid(String text);

        /**
         * Returns the error message to be shown if the constraint is not met.
         *
         * @return the error message.
         */
        String getErrorMessage();
    }

    /**
     * An inner class that encapsulates an EditText field along with its associated validation constraints.
     */
    public class EditTextValidator {
        private final EditText editText;
        private final List<Constraint> constraints = new ArrayList<>();
        private ExpectedType expectedType = ExpectedType.NONE;

        /**
         * Constructs a EditTextValidator for the given EditText.
         *
         * @param editText the EditText to be validated.
         */
        public EditTextValidator(EditText editText) {
            this.editText = editText;
        }

        /**
         * Returns the underlying EditText.
         *
         * @return the EditText.
         */
        public EditText getEditText() {
            return editText;
        }

        /**
         * Adds a constraint that the text must be a valid integer.
         *
         * @return this EditTextValidator for chaining.
         */
        public EditTextValidator validInt() {
            expectedType = ExpectedType.INT;
            constraints.add(new Constraint() {
                @Override
                public boolean isValid(String text) {
                    try {
                        Integer.parseInt(text);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }

                @Override
                public String getErrorMessage() {
                    return "Must be a valid integer";
                }
            });
            return this;
        }

        /**
         * Adds a constraint that the text must be a valid number (double).
         *
         * @return this EditTextValidator for chaining.
         */
        public EditTextValidator validDouble() {
            expectedType = ExpectedType.DOUBLE;
            constraints.add(new Constraint() {
                @Override
                public boolean isValid(String text) {
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }

                @Override
                public String getErrorMessage() {
                    return "Must be a valid number";
                }
            });
            return this;
        }

        /**
         * Adds a constraint that the text must not be empty.
         *
         * @return this EditTextValidator for chaining.
         */
        public EditTextValidator notEmpty() {
            expectedType = ExpectedType.STRING;
            constraints.add(new Constraint() {
                @Override
                public boolean isValid(String text) {
                    return text != null && !text.trim().isEmpty();
                }

                @Override
                public String getErrorMessage() {
                    return "Cannot be empty";
                }
            });
            return this;
        }

        /**
         * Adds a constraint that the numeric value must be greater than the specified threshold.
         *
         * @param threshold the threshold value.
         * @return this EditTextValidator for chaining.
         */
        public EditTextValidator greaterThan(final double threshold) {
            constraints.add(new Constraint() {
                @Override
                public boolean isValid(String text) {
                    try {
                        if (expectedType == ExpectedType.INT) {
                            int value = Integer.parseInt(text);
                            return value > threshold;
                        } else if (expectedType == ExpectedType.DOUBLE) {
                            double value = Double.parseDouble(text);
                            return value > threshold;
                        }
                        return false;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }

                @Override
                public String getErrorMessage() {
                    return "Must be greater than " + threshold;
                }
            });
            return this;
        }

        /**
         * Validates the text in the EditText against all added constraints.
         * If a constraint fails, sets an error message on the EditText.
         *
         * @return true if all constraints are satisfied; false otherwise.
         */
        public boolean validate() {
            String text = editText.getText().toString();
            for (Constraint constraint : constraints) {
                if (!constraint.isValid(text)) {
                    editText.setError(constraint.getErrorMessage());
                    return false;
                }
            }
            return true;
        }

        /**
         * Retrieves the integer value of the EditText's text.
         *
         * @return the parsed integer.
         * @throws NumberFormatException if the text is not a valid integer.
         */
        public int getInt() {
            return Integer.parseInt(editText.getText().toString());
        }

        /**
         * Retrieves the double value of the EditText's text.
         *
         * @return the parsed double.
         * @throws NumberFormatException if the text is not a valid double.
         */
        public double getDouble() {
            return Double.parseDouble(editText.getText().toString());
        }

        /**
         * Retrieves the text from the EditText.
         *
         * @return the text string.
         */
        public String getString() {
            return editText.getText().toString();
        }
    }
}
