package com.p4rc.sdk.model;

import java.io.Serializable;

import org.json.JSONObject;

import com.p4rc.sdk.utils.JsonUtility;


public class Point implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int level;
	private int p4rcPoints;
	private int gameScore;
	
	public Point() {}

	public Point(int level, int p4rcPoints, int gameScore) {
		this.level = level;
		this.p4rcPoints = p4rcPoints;
		this.gameScore = gameScore;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getP4rcPoints() {
		return p4rcPoints;
	}

	public void setP4rcPoints(int p4rcPoints) {
		this.p4rcPoints = p4rcPoints;
	}

	public int getGameScore() {
		return gameScore;
	}

	public void setGameScore(int gameScore) {
		this.gameScore = gameScore;
	}
	
	public static Point parse(JSONObject dataJSON) {
		if(dataJSON == null) {
			return null;
		}
		int level = dataJSON.optInt(JsonUtility.LEVEL_PARAM);
		int points = dataJSON.optInt(JsonUtility.P4RC_POINTS);
		int gameScore = dataJSON.optInt(JsonUtility.GAME_SCORE_PARAM);
		
		return new Point(level, points, gameScore);
	}

	@Override
	public String toString() {
		return "Point [level=" + level + ", p4rcPoints=" + p4rcPoints
				+ ", gameScore=" + gameScore + "]";
	}
	
}
