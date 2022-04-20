package com.edss.hitsales.Utils;

import android.widget.EditText;
import android.widget.TextView;

public class MyValidator {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String REQUIRED_MSG = "Field required";

    public static boolean isValidField(TextView textView) {
        String txtValue = textView.getText().toString().trim();

        if (txtValue.length() == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        }
        textView.setError(null);
        return true;
    }

    public static boolean isValidField(EditText editText) {
        String txtValue = editText.getText().toString().trim();

        if (txtValue.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        editText.setError(null);
        return true;
    }

    public static boolean isValidMobile(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length() == 10) {
            editText.setError(null);
            return true;
        }
        editText.setError(REQUIRED_MSG + " Enter 10 digits");
        return false;
    }

}
