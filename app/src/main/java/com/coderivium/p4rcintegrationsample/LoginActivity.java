package com.coderivium.p4rcintegrationsample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.p4rc.sdk.OnLoginWithEmailCallback;
import com.p4rc.sdk.P4RC;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailTextInputLayout, passwordTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        P4RC.getInstance().setContext(this);

        emailTextInputLayout = findViewById(R.id.email_text_input_layout);
        passwordTextInputLayout = findViewById(R.id.password_text_input_layout);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(emailTextInputLayout.getEditText()).getText().toString();
            String password = Objects.requireNonNull(passwordTextInputLayout.getEditText()).getText().toString();

            P4RC.getInstance().userLogin(email, password, new OnLoginWithEmailCallback() {
                @Override
                public void onCompleted(String error) {
                    if (error != null) {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
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