package com.coderivium.p4rcintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.p4rc.sdk.P4RC;

public class DashboardActivity extends AppCompatActivity {

    private TextView userTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        P4RC.getInstance().setContext(this);

        userTextView = findViewById(R.id.userText);

        userTextView.setText(P4RC.getInstance().getUser().toString());

    }
}