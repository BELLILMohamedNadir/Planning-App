package com.example.planning.utils;

import android.text.InputType;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordVisibility {

    public static void managePasswordVisibility(TextInputLayout edtLayout, TextInputEditText edt){
        edtLayout.setEndIconOnClickListener(v -> {
            // Toggle password visibility
            if (edt.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                edt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            // Move the cursor to the end of the text after changing the input type
            edt.setSelection(edt.getText().length());
        });
    }
}
