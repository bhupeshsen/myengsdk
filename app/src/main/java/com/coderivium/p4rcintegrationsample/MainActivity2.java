package com.coderivium.p4rcintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class MainActivity2 extends AppCompatActivity {
    Button materialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        materialButton = findViewById(R.id.webViewBtn);
        materialButton.setOnClickListener(v->{
            Intent intent = new Intent(this, WebMyXRSDK.class);
            startActivity(intent);
        });

    }
}