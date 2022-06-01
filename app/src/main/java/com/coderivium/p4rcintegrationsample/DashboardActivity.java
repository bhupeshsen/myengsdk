package com.coderivium.p4rcintegrationsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.task.CustomAsyncTask;
import com.p4rc.sdk.task.GameListTask;

public class DashboardActivity extends AppCompatActivity {

    private TextView userTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        P4RC.getInstance().setContext(this);

        userTextView = findViewById(R.id.userText);

        GameListTask gameListTask = new GameListTask(this);
        gameListTask.setAsyncTaskListener(new CustomAsyncTask.AsyncTaskListener() {
            @Override
            public void onBeforeTaskStarted(CustomAsyncTask<?, ?, ?> task) {

            }

            @Override
            public void onTaskFinished(CustomAsyncTask<?, ?, ?> task) {
                userTextView.setText(gameListTask.getData().getData().toString());
            }
        });
        gameListTask.execute();

//        userTextView.setText(P4RC.getInstance().getUser().toString());

    }
}