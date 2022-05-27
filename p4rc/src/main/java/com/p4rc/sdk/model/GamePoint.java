package com.p4rc.sdk.model;

import java.io.Serializable;

import com.p4rc.sdk.utils.Constants;

public class GamePoint implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private long startTime;
	private long endTime;
	private long transTime;
	private int level;
	private int minutesPlayed;
	private int gamePoints;
	
	public GamePoint() {}
	
	public GamePoint(long startTime, long endTime, int level, int minutesPlayed,
			int gamePoints, long transactionTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.level = level;
		this.minutesPlayed = minutesPlayed;
		this.gamePoints = gamePoints;
		this.transTime = transactionTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public int getGamePoints() {
		return gamePoints;
	}

	public void setGamePoints(int gamePoints) {
		this.gamePoints = gamePoints;
	}
	
	public long getTransTime() {
		return transTime;
	}

	public void setTransTime(long transTime) {
		this.transTime = transTime;
	}
	
	@Override
	public String toString() {
		return "GamePoint [startTiem=" + startTime + ", endTime=" + endTime
				+ ", level=" + level + ", minutesPlayed=" + minutesPlayed +
				", gamePoints=" + gamePoints + " transTime=" + transTime + "]";
	}
	
	public static int calculatePlayedTime(long startTime, long endTime) {
		return (int) (endTime - startTime) / 
			(Constants.MILIS_IN_SECOND * Constants.SEC_IN_MINUTE);
	}
}
