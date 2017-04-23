package com.banglacricket.bdcricketteam.util;

import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Ripon
 */

public class Validator {

    public static boolean validateNotEmpty(EditText editText, String errorMsg) {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.setError(errorMsg);
            // in case user fills only blanks (which is visible in password fields), simply blank out the field.
            editText.setText("");

            return false;
        } else {
            editText.setError(null);

            return true;
        }
    }

    public static boolean validateInputsMatch(EditText editText1, EditText editText2, String errorMsg) {
        if (!editText1.getText().toString().equals(editText2.getText().toString())) {
            editText1.setError(errorMsg);

            return false;
        } else {
            editText1.setError(null);

            return true;
        }
    }

    public static void setFieldError(TextView textView, String errorMsg) {
        textView.setError(errorMsg);

        if (errorMsg != null) {
            textView.setFocusableInTouchMode(true);
        }
    }
}
