package com.coderivium.p4rcintegrationsample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p4rc.sdk.OnGamePointsCallback;
import com.p4rc.sdk.OnLoginWithEmailCallback;
import com.p4rc.sdk.OnPointsIncrementCallback;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.gamelist.Game;
import com.p4rc.sdk.model.gamelist.GamePoints;

public class GameActivity extends AppCompatActivity {

    private Game game;
    int level;
    int points;
    private int finishStep;
    private AlertDialog dialog;


    private Button startBtn, stopBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (getIntent().hasExtra("game")) {
            game = (Game) getIntent().getSerializableExtra("game");
        } else {
            Toast.makeText(this, "No game selected", Toast.LENGTH_SHORT).show();
            finish();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(game.getName());
        toolbar.setNavigationOnClickListener(view -> finish());


        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);

        startBtn.setOnClickListener(view-> startGame());
        stopBtn.setOnClickListener(view-> stopGame());
        stopBtn.setEnabled(false);
    }

    public void stopGame() {
        if (P4RC.getInstance().isLevelStarted()) {
            P4RC.getInstance().stopGame(game.getGameRefId(), new OnPointsIncrementCallback() {
                @Override
                public void onCompleted(String error) {
                    if (error != null) {
                        Toast.makeText(GameActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onValidationError(String error) {
                    Toast.makeText(GameActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();


                }
            });
           // startLevelFinishDialog().show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_press_start_level),
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void startGame() {
        P4RC.getInstance().startLevelWithPoints(1, 100);
        P4RC.getInstance().didStartLevel();
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
    }

    private AlertDialog startLevelFinishDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle(getResources().getString(R.string.alert_dialog_finished));

        RelativeLayout wrapper = new RelativeLayout(GameActivity.this);
        final TextView text = new TextView(GameActivity.this);
        text.setText(getResources().getString(R.string.dialog_enter_level));
        text.setId(View.generateViewId());
        final EditText input = new EditText(GameActivity.this);
        input.setId(View.generateViewId());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL, text.getId());
        textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, text.getId());
        wrapper.addView(text, textParams);

        RelativeLayout.LayoutParams inputParams = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        inputParams.addRule(RelativeLayout.BELOW, text.getId());
        inputParams.addRule(RelativeLayout.CENTER_HORIZONTAL, input.getId());

        Button okBtn = new Button(GameActivity.this);
        okBtn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        okBtn.setId(View.generateViewId());
        okBtn.setText(getResources().getString(R.string.dialog_button_ok));
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (finishStep == 0) {
                    if ("".equals(input.getText().toString())) {
                        return;
                    }
                    finishStep = finishStep + 1;

                    level = input.getText().toString().length() > 10 ? Integer.MAX_VALUE
                            : Integer.parseInt(input.getText().toString());

                    text.setText(getResources().getString(R.string.dialog_enter_point));
                    input.setText("");
                    return;
                } else if (finishStep == 1) {
                    if ("".equals(input.getText().toString())) {
                        return;
                    }
                    finishStep = 0;
                    points = input.getText().toString().length() > 10 ? Integer.MAX_VALUE
                            : Integer.parseInt(input.getText().toString());
                    P4RC.getInstance().didCompleteLevelWithPoints(level, points);
                    Toast.makeText(GameActivity.this, "Points Added", Toast.LENGTH_SHORT).show();
                    level = 0;
                    points = 0;
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                    dialog.dismiss();
                }
            }
        });
        Button cancelBtn = new Button(GameActivity.this);
        cancelBtn.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout
                .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        cancelBtn.setText(getResources().getString(R.string.dialog_button_cancel));
        cancelBtn.setId(View.generateViewId());
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RelativeLayout.LayoutParams okBtnPar = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        okBtnPar.addRule(RelativeLayout.ALIGN_PARENT_LEFT, okBtn.getId());
        okBtnPar.addRule(RelativeLayout.BELOW, input.getId());

        RelativeLayout.LayoutParams cancelBtnPar = new RelativeLayout.LayoutParams(RelativeLayout.
                LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cancelBtnPar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, cancelBtn.getId());
        cancelBtnPar.addRule(RelativeLayout.BELOW, input.getId());

        wrapper.addView(input, inputParams);
        wrapper.addView(okBtn, okBtnPar);
        wrapper.addView(cancelBtn, cancelBtnPar);
        builder.setView(wrapper);

        dialog = builder.create();
        return dialog;
    }
}