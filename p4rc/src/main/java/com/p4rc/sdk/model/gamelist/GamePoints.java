package com.p4rc.sdk.model.gamelist;

import com.p4rc.sdk.model.AuthSession;
import com.p4rc.sdk.model.User;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 31/07/20 at 2:16 PM.
 * bhupeshsen11@gmail.com
 * 7974430255
 */
public class GamePoints implements Serializable {
    private float debitedPoints;
    private float creditedPoints;
    private float balancePoints;
    private float totalLifeTimePoints;
    private float soonExpiringPoints;
    private String name;


    // Getter Methods

    public float getDebitedPoints() {
        return debitedPoints;
    }

    public float getCreditedPoints() {
        return creditedPoints;
    }

    public float getBalancePoints() {
        return balancePoints;
    }

    public float getTotalLifeTimePoints() {
        return totalLifeTimePoints;
    }

    public float getSoonExpiringPoints() {
        return soonExpiringPoints;
    }

    public String getName() {
        return name;
    }

    // Setter Methods

    public void setDebitedPoints(float debitedPoints) {
        this.debitedPoints = debitedPoints;
    }

    public void setCreditedPoints(float creditedPoints) {
        this.creditedPoints = creditedPoints;
    }

    public void setBalancePoints(float balancePoints) {
        this.balancePoints = balancePoints;
    }

    public void setTotalLifeTimePoints(float totalLifeTimePoints) {
        this.totalLifeTimePoints = totalLifeTimePoints;
    }

    public void setSoonExpiringPoints(float soonExpiringPoints) {
        this.soonExpiringPoints = soonExpiringPoints;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GamePoints fromJSON(JSONObject json) {
        GamePoints gamepoints = new GamePoints();
        gamepoints.setDebitedPoints(json.optInt("debitedPoints"));
        gamepoints.setCreditedPoints(json.optInt("creditedPoints"));
        gamepoints.setBalancePoints(json.optInt("balancePoints"));
        gamepoints.setTotalLifeTimePoints(json.optInt("totalLifeTimePoints"));
        gamepoints.setSoonExpiringPoints(json.optInt("soonExpiringPoints"));
        gamepoints.setName(json.optString("name"));
        return gamepoints;
    }


}
