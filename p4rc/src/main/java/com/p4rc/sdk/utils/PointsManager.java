package com.p4rc.sdk.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.crypto.CipherPreferencesEx;
import com.p4rc.sdk.model.GamePoint;
import com.p4rc.sdk.model.Point;

public class PointsManager {
	private final static String TAG = "PointsTableStorage";

	private final static String FILE_NAME = "points_table.dat";
	private final static String GAME_POINTS_FILE = "game_points.dat";

	private final static String MAX_POINTS_PARAM_NAME = "max_points";
	private final static String LAST_GAME_POINTS = "last_points";
	private final static String LAST_P4RC_POINTS = "last_p4rc_points";
	private final static String TOTAL_GAME_POINTS = "total_points";
	private final static String TOTAL_P4RC_POINTS = "total_p4rc_points";
	private final static String LAST_PLAYED_LEVEL = "last_played_level";

	private static PointsManager instance = null;

	private Context context;

	private SharedPreferences cipherPrefs;

	private HashMap<Integer, Point> pointsTable = null;
	private ArrayList<GamePoint> gamePointArrayList = null;

	private int lastP4RCPoints;
	private int totalP4RCPoints;
	private int lastGamePoints;
	private int totalGamePoints;
	private long lastLevelStartTime;
	private int maxPoints;
	private int lastLevel;

	private Charset CHARSET = Charset.forName("ISO-8859-1");
	private String key = "1234567812345678";

	private byte[] ivbytes = new byte[] { (byte) 'a', (byte) 'b', (byte) 'c',
			(byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h',
			(byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm',
			(byte) 'n', (byte) 'o', (byte) 'p' };

	private IvParameterSpec iv = new IvParameterSpec(ivbytes);

	private PointsManager(Context context){
		this.context = context;
		if(gamePointArrayList == null) {
			gamePointArrayList = new ArrayList<GamePoint>();
			
			if(getFlagFirstLoad() == false){
				saveGamePoints();
				setFlagFirstLoad();
			}
		}
		
		if(pointsTable == null) {
			pointsTable = new HashMap<Integer, Point>();
		}
		
		loadPoints();
		loadGamePoints();
	}

	public static PointsManager getInstance(Context context) {
		if (instance == null) {
			synchronized (PointsManager.class) {
				if (instance == null) {
					instance = new PointsManager(context);
				}
			}
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private boolean load(String fileName) {
		CipherInputStream cis = null;
		ObjectInputStream oin = null;
		try {
//			byte[] encoded = getRawKey(key.getBytes(CHARSET));
//			SecretKey seckey = new SecretKeySpec(encoded, "AES");
			SecretKey seckey = new SecretKeySpec(key.getBytes(CHARSET), "AES");
			Cipher encipher = Cipher.getInstance("AES/CFB8/NoPadding");
			encipher.init(Cipher.ENCRYPT_MODE, seckey, iv);

			FileInputStream fis = context.openFileInput(fileName);
			cis = new CipherInputStream(fis, encipher);
			oin = new ObjectInputStream(cis);
			
			if (FILE_NAME.equals(fileName)) {
				pointsTable = (HashMap<Integer, Point>) oin.readObject();
				return true;
			} else if (GAME_POINTS_FILE.equals(fileName)) {
				gamePointArrayList = (ArrayList<GamePoint>) oin.readObject();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (oin != null) {
					oin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean save(String fileName, Object objectForSave) {
		FileOutputStream fos = null;
		CipherOutputStream cos = null;
		ObjectOutputStream oos = null;
		try {
//			byte[] encoded = getRawKey(key.getBytes(CHARSET));
//			SecretKey seckey = new SecretKeySpec(encoded, "AES");
			SecretKey seckey = new SecretKeySpec(key.getBytes(CHARSET), "AES");
			Cipher encipher = Cipher.getInstance("AES/CFB8/NoPadding");
			encipher.init(Cipher.DECRYPT_MODE, seckey, iv);

			fos = context.openFileOutput(fileName, 0);
			cos = new CipherOutputStream(fos, encipher);
			oos = new ObjectOutputStream(cos);
			oos.writeObject(objectForSave);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean saveGamePoints() {
		return save(GAME_POINTS_FILE, gamePointArrayList);
	}

	public boolean savePoints() {
		return save(FILE_NAME, pointsTable);
	}

	public boolean loadGamePoints() {
		return load(GAME_POINTS_FILE);
	}

	public boolean loadPoints() {
		return load(FILE_NAME);
	}

	private byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(256, sr);
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	public void printToLog() {
		if (pointsTable == null) {
			loadPoints();
		}

		HashMap<Integer, Point> data = pointsTable;
		if (data != null) {
			Collection<Point> pointList = data.values();
			for (Point point : pointList) {
				Log.d(TAG, point.toString());
			}
		} else {
			Log.d(TAG, "Error loading points table");
		}
	}

	public void convertPointsToP4RCPoints(int points, int level) {
		 if (pointsTable == null){
			 return;
		 }
		 if (pointsTable.get(level) == null){
			 return;
		 }
		
		if (level > pointsTable.size() || level <= 0) {
			return;
		}
		
		float gameScore = pointsTable.get(level).getGameScore();
		float p4rcPoints = pointsTable.get(level).getP4rcPoints();
		float p4rcCoef = gameScore / p4rcPoints;
		int newP4RCPoints = (int)(points / p4rcCoef);
		
		if (newP4RCPoints > getMaxPoints()){
			newP4RCPoints = getMaxPoints();
		}
		
		setLastP4RCPoints(newP4RCPoints);
		setTotalP4RCPoints(getTotalP4RCPoints() + newP4RCPoints);
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
		cipherPrefs = new CipherPreferencesEx(context,
				context.getSharedPreferences("SECURE", Context.MODE_PRIVATE));
	}

	public HashMap<Integer, Point> getPointsTable() {
		return pointsTable;
	}

	public void setPointsTable(HashMap<Integer, Point> pointsTable) {
		this.pointsTable = pointsTable;
	}
	
	public GamePoint getGamePoint(int level) {
		for (ListIterator iterator = gamePointArrayList.listIterator(gamePointArrayList.size()); iterator.hasPrevious();) {
			final GamePoint eachGamePoint = (GamePoint) iterator.previous();
			if(eachGamePoint.getLevel() == level){
				return eachGamePoint;
			}
		}
		return null;
	}

	public void addGamePoint(GamePoint point){
		this.gamePointArrayList.add(point);
	}

	public void removeLevelPointsData(long lastLevelStartTime) {
		for (GamePoint eachGamePoint : gamePointArrayList) {
			if(eachGamePoint.getStartTime() == lastLevelStartTime){
				this.gamePointArrayList.remove(eachGamePoint);
			}
		}
	}

	public ArrayList<GamePoint> getGamePointsTable() {
		return gamePointArrayList;
	}

	public void resetAllGamePoints() {
		this.gamePointArrayList.clear();
	}
	
	public int getLastP4RCPoints() {
		if(this.lastP4RCPoints == 0) {
			this.lastP4RCPoints = cipherPrefs.getInt(LAST_P4RC_POINTS, 0);
		}
		return lastP4RCPoints;
	}

	public void resetP4RCPoints() {
		this.lastP4RCPoints = 0;
		this.totalP4RCPoints = 0;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(TOTAL_P4RC_POINTS, 0);
		editor.putInt(LAST_P4RC_POINTS, 0);
		editor.commit();
	}

	public int getTotalP4RCPoints() {
		if(this.totalP4RCPoints == 0) {
			this.totalP4RCPoints = cipherPrefs.getInt(TOTAL_P4RC_POINTS, 0);
		}
		return this.totalP4RCPoints;
	}

	public long getLastLevelStartTime() {
		return this.lastLevelStartTime;
	}
	
	public void setLastLevelStartTime(long startTime) {
		this.lastLevelStartTime = startTime;
	}

	public void setLastP4RCPoints(int lastP4RCPoints) {
		this.lastP4RCPoints = lastP4RCPoints;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(LAST_P4RC_POINTS, this.lastP4RCPoints);
		editor.commit();
	}

	public void setTotalP4RCPoints(int totalP4RCPoints) {
		this.totalP4RCPoints = totalP4RCPoints;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(TOTAL_P4RC_POINTS, this.totalP4RCPoints);
		editor.commit();
	}
	
	public void setLastGamePoints(int lastPoints) {
		this.lastGamePoints = lastPoints;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(LAST_GAME_POINTS, this.lastGamePoints);
		editor.commit();
	}
	
	public void setTotalGamePoints(int totalPoints) {
		this.totalGamePoints = totalPoints;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(TOTAL_GAME_POINTS, this.totalGamePoints);
		editor.commit();
	}

	public int getLastGamePoints() {
		if(this.lastGamePoints == 0) {
			this.lastGamePoints = cipherPrefs.getInt(LAST_GAME_POINTS, 0);
		}
		return this.lastGamePoints;
	}

	public int getTotalGamePoints() {
		if(this.totalGamePoints == 0) {
			this.totalGamePoints = cipherPrefs.getInt(TOTAL_GAME_POINTS, 0);
		}
		return this.totalGamePoints;
		
	}
	
	public void resetGamePOints() {
		this.lastGamePoints = 0;
		this.totalGamePoints = 0;
		SharedPreferences.Editor edit = cipherPrefs.edit();
		edit.putInt(LAST_GAME_POINTS, 0);
		edit.putInt(TOTAL_GAME_POINTS, 0);
		edit.commit();
	}

	public int getMaxPoints() {
		if(maxPoints == 0) {
			maxPoints = cipherPrefs.getInt(MAX_POINTS_PARAM_NAME,
					AppConfig.DEFAULT_MAX_POINTS);
		}
		return maxPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
		SharedPreferences.Editor edit = cipherPrefs.edit();
		edit.putInt(MAX_POINTS_PARAM_NAME, maxPoints);
		edit.commit();
	}

	public void setLastLevelValue(int level) {
		this.lastLevel = level;
		SharedPreferences.Editor editor = cipherPrefs.edit();
		editor.putInt(LAST_PLAYED_LEVEL, this.lastLevel);
		editor.commit();
	}

	public int getLastLevelValue() {
		if(this.lastLevel == 0) {
			this.lastLevel = cipherPrefs.getInt(LAST_PLAYED_LEVEL, 0);
		}
		return this.lastLevel;
	}

	public long getLastPlayerPingTime() {
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		long time = prefs.getLong(AppConfig.LAST_PLAYER_PING_TIME_PARAM, 0);
		return time;
	}

	public void setLastPlayerPingTime(long time) {
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong(AppConfig.LAST_PLAYER_PING_TIME_PARAM, time);
		editor.commit();
	}
	
	public int getLastPlayerPingDay() {
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		int day = prefs.getInt(AppConfig.LAST_PLAYER_PING_DAY_PARAM, -1);
		return day;
	}

	public void setLastPlayerPingDay(int day) {
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(AppConfig.LAST_PLAYER_PING_DAY_PARAM, day);
		editor.commit();
	}
	
	public void setLastPlayerUnixTime(long unixTime){
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong(AppConfig.LAST_PLAYER_PING_UNIX_TIME, unixTime);
		editor.commit();
	}
	
	public long getLastPlayerUnixTime(){
		final SharedPreferences prefs = context.getSharedPreferences(
				AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		long unixTime = prefs.getLong(AppConfig.LAST_PLAYER_PING_UNIX_TIME, -1);
		return unixTime;
	}

	public boolean isPointsTableExists() {
		return pointsTable != null;
	}
	
	private void setFlagFirstLoad(){
		final SharedPreferences prefs = context.getSharedPreferences(AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(AppConfig.FLAG_FIRST_LOAD, true);
		editor.commit();
	}
	
	private boolean getFlagFirstLoad(){
		final SharedPreferences prefs = context.getSharedPreferences(AppConfig.PREFERENCE, Context.MODE_PRIVATE);
		boolean flagFirstLoad = prefs.getBoolean(AppConfig.FLAG_FIRST_LOAD, false);
		return flagFirstLoad;
	}
}
