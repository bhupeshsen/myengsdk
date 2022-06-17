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
import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.OnGameListCallback;
import com.p4rc.sdk.OnGamePointsCallback;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.User;
import com.p4rc.sdk.model.gamelist.Game;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.model.gamelist.GamePoints;
import com.p4rc.sdk.task.CustomAsyncTask;
import com.p4rc.sdk.task.GameListTask;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class DashboardActivity extends AppCompatActivity {
    TextView p4rcPoints;
    private GameListAdapter adapter;
    private List<Game> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        P4RC.getInstance().setContext(this);
        p4rcPoints = findViewById(R.id.p4rcPoints);
        setUserData(P4RC.getInstance().getUser());

        setButtonCallbacks();
        list = new ArrayList<>();
        adapter = new GameListAdapter(this, list, new GameListAdapter.OnGameSelectListener() {
            @Override
            public void onGameSelected(Game game) {

                if(game.getGameRefId()!=null){
                    Intent intent = new Intent(DashboardActivity.this, GameActivity.class);
                    intent.putExtra("game", game);
                    startActivity(intent);
                }else{
                    Toast.makeText(DashboardActivity.this, "Null Game  id ", Toast.LENGTH_SHORT).show();

                }

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

    private  void gamePoint(){
        P4RC.getInstance().getUserPoints(AppConfig.
                getInstance().getUTFSessionId(),new OnGamePointsCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(GamePoints gamePoints) {
                if (gamePoints != null) {

                    p4rcPoints.setText("P4RC Points: " +String.valueOf(gamePoints.getCreditedPoints()));
                }
            }
            @Override
            public void onError(int errorCode, String message) {
                Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setButtonCallbacks(){
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            P4RC.getInstance().logout();
            finish();
        });
        findViewById(R.id.refreshButton).setOnClickListener(v -> {
            gamePoint();
        });
    }


    @SuppressLint("SetTextI18n")
    private void setUserData(User user){
        TextView userTextView = findViewById(R.id.userNameTextView);

        TextView totalPoints = findViewById(R.id.totalPoints);

        userTextView.setText("Hi, " + user.getFirstName()+" "+user.getLastName());

        totalPoints.setText("Total Points: " + user.getTotalPoints());


    }
}