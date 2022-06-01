
package com.p4rc.sdk.model.gamelist;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameList {

    private Meta meta;
    private List<Game> games = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }


    public static GameList fromJSON(JSONObject jsonObject) {
        GameList gameList = new GameList();
        gameList.setMeta(Meta.fromJSON(jsonObject.optJSONObject("meta")));
        JSONArray games = jsonObject.optJSONArray("games");
        if (games != null) {
            List<Game> gameList1 = new ArrayList<>();
            for (int i = 0; i < games.length(); i++) {
                gameList1.add(Game.fromJSON(games.optJSONObject(i)));
            }
            gameList.setGames(gameList1);
        }
        return gameList;
    }

    @Override
    public String toString() {
        return "GameList{" +
                "meta=" + meta +
                ", games=" + games +
                '}';
    }
}
