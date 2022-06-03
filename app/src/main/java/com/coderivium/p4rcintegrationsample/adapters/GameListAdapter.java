package com.coderivium.p4rcintegrationsample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coderivium.p4rcintegrationsample.R;
import com.p4rc.sdk.model.gamelist.Game;

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ItemViewHolder> {

    private final Context context;
    private final List<Game> list;

    public GameListAdapter(Context context, List<Game> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Game game = list.get(position);
        holder.gameTitle.setText(game.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView gameTitle = itemView.findViewById(R.id.gameTitle);
    }
}
