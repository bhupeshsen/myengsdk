package com.coderivium.p4rcintegrationsample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.p4rc.sdk.OnLoginWithEmailCallback;
import com.p4rc.sdk.P4RC;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout firstNameTextInput, lastNameTextInput, emailTextInputLayout, passwordTextInputLayout, dobTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        P4RC.getInstance().setContext(this);

        firstNameTextInput = findViewById(R.id.first_name_text_input_layout);
        lastNameTextInput = findViewById(R.id.last_name_text_input_layout);
        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        dobTextInput = findViewById(R.id.dob_text_input_layout);
        Button signupButton = findViewById(R.id.signup_button);
        findViewById(R.id.login_redirect).setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String firstName = Objects.requireNonNull(firstNameTextInput.getEditText()).getText().toString();
            String lastName = Objects.requireNonNull(lastNameTextInput.getEditText()).getText().toString();
            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString();
            String dob = Objects.requireNonNull(dobTextInput.getEditText()).getText().toString();

            P4RC.getInstance().userSignup(firstName, lastName,email, password, "Android", dob, new OnLoginWithEmailCallback() {
                @Override
                public void onCompleted(String error) {
                    if (error != null) {
                        Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onValidationError(String emailError, String passwordError) {
                    if (emailError != null) {
                        emailTextInputLayout.setError(emailError);
                    }
                    if (passwordError != null) {
                        passwordTextInputLayout.setError(passwordError);
                    }
                }
            });
        });


    }
}