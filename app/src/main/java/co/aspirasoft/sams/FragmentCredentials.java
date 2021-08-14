package co.aspirasoft.sams;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FragmentCredentials extends StepFragment {

    private TextInputLayout usernameWrapper;
    private TextInputEditText usernameField;

    private TextInputLayout passwordWrapper;
    private TextInputEditText passwordField;

    private TextInputLayout confirmWrapper;
    private TextInputEditText confirmField;

    public static StepFragment newInstance() {
        return new FragmentCredentials();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credentials, container, false);

        usernameWrapper = view.findViewById(R.id.username_wrapper);
        usernameField = view.findViewById(R.id.username_field);
        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldNotEmpty(usernameWrapper, usernameField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordWrapper = view.findViewById(R.id.password_wrapper);
        passwordField = view.findViewById(R.id.password_field);
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldNotEmpty(passwordWrapper, passwordField);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmWrapper = view.findViewById(R.id.confirmation_wrapper);
        confirmField = view.findViewById(R.id.comfirmation_field);
        confirmField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordMatch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private boolean validatePasswordMatch() {
        confirmWrapper.setErrorEnabled(false);
        if (passwordField.getText() != null && confirmField.getText() != null &&
                !confirmField.getText().toString().equals(passwordField.getText().toString())
                && !confirmField.getText().toString().isEmpty()) {
            confirmWrapper.setError("Passwords do not match.");
            confirmWrapper.setErrorEnabled(true);
            return false;
        }

        return true;
    }

    @Override
    public boolean isDataValid() {
        if (checkFieldNotEmpty(usernameWrapper, usernameField) &&
                checkFieldNotEmpty(passwordWrapper, passwordField) &&
                checkFieldNotEmpty(confirmWrapper, confirmField) &&
                validatePasswordMatch()) {
            data.put(R.id.username_field, usernameField.getText().toString().trim());
            data.put(R.id.password_field, passwordField.getText().toString().trim());
            return true;
        } else {
            return false;
        }
    }

}