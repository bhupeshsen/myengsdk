package com.coderivium.p4rcintegrationsample;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.p4rc.sdk.P4RC;
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
                Toast.makeText(DashboardActivity.this, "DONE " + task, Toast.LENGTH_SHORT).show();
                if (gameListTask.getData() != null) {
                    Log.d("TAG", "onTaskFinished: " + task.getResult());;
                    Log.d("TAG", "onTaskFinished: HEre");
                    if (gameListTask.getData().getData() != null)
                        userTextView.setText(gameListTask.getData().getData().toString());
                }
            }
        });
        gameListTask.execute();

//        userTextView.setText(P4RC.getInstance().getUser().toString());

    }
}