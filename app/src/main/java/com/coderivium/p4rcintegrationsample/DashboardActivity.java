package com.coderivium.p4rcintegrationsample;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coderivium.p4rcintegrationsample.adapters.GameListAdapter;
import com.p4rc.sdk.OnGameListCallback;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.User;
import com.p4rc.sdk.model.gamelist.Game;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.task.CustomAsyncTask;
import com.p4rc.sdk.task.GameListTask;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class DashboardActivity extends AppCompatActivity {

    private GameListAdapter adapter;
    private List<Game> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        P4RC.getInstance().setContext(this);

        setUserData(P4RC.getInstance().getUser());

        setButtonCallbacks();
        list = new ArrayList<>();
        adapter = new GameListAdapter(this, list, new GameListAdapter.OnGameSelectListener() {
            @Override
            public void onGameSelected(Game game) {
                Intent intent = new Intent(DashboardActivity.this, GameActivity.class);
                intent.putExtra("game", game);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.gameListRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadGameList();

    }

    private void loadGameList(){
        ProgressDialog progressDialog = Utils.buildLoading(this);
        progressDialog.show();
        P4RC.getInstance().loadGameList(new OnGameListCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(GameList gameList) {
                if (gameList != null) {
                    Log.d("TAG", "onSuccess: " + gameList.toString());
                    list.clear();
                    list.addAll(gameList.getGames());
                    adapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(int errorCode, String message) {
                Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setButtonCallbacks(){
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            P4RC.getInstance().logout();
            finish();
        });
        findViewById(R.id.refreshButton).setOnClickListener(v -> {
            loadGameList();
        });
    }


    @SuppressLint("SetTextI18n")
    private void setUserData(User user){
        TextView userTextView = findViewById(R.id.userNameTextView);
        TextView p4rcPoints = findViewById(R.id.p4rcPoints);
        TextView totalPoints = findViewById(R.id.totalPoints);

        userTextView.setText("Hi, " + user.getFirstName());
        p4rcPoints.setText("P4RC Points: " + user.getTotalPoints());
        totalPoints.setText("Total Points: " + user.getTotalPoints());


    }
}