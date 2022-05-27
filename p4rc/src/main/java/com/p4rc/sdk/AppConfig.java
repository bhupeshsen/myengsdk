package com.p4rc.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.p4rc.sdk.utils.JsonUtility;

import java.util.Arrays;
import java.util.List;

public class AppConfig {

	public static final String PRODUCTION_SERVER_HOST = BuildConfig.SERVER_URL;
	public static final String TEST_RESOURCES_SERVER = "http://test.p4rc.com:8080/resources/wl/index.html";

	public static final String DEFAULT_SERVER_HOST = PRODUCTION_SERVER_HOST;

	public static final String DEFAULT_SERVER_ADDRESS = BuildConfig.FRONTEND_URL;
//	public static final String DEFAULT_SERVER_ADDRESS = DEFAULT_SERVER_HOST + "/resources/marvel/index.html";

	public static final int DEFAULT_MAX_POINTS = 100;
	
	public static final int FRAMED_BROWSER_TIMEOUT = 15;
	public static final int REQUEST_TIMEOUT = 60;
	
	public static final String PREFERENCE = "com.p4rc.sdk";

	public static final String[] FACEBOOK_PERMISSIONS = { "email"};
	public static final List<String> PERMISSIONS = Arrays.asList("email");
	
	public static final String LAST_LEVEL_PLAYED_PARAM = "last_level_played";
	
	public static final String LAST_P4RC_POINTS_PARAM = "last_p4rc_points";
	
	public static final String LAST_PLAYER_PING_TIME_PARAM = "last_player_ping_time";
	public static final String LAST_PLAYER_PING_DAY_PARAM = "last_player_ping_day";
	public static final String FLAG_FIRST_LOAD = "FlagFirstLoad";
	
	public static final String LAST_PLAYER_PING_UNIX_TIME = "last_player_ping_unix_time";
	
	private static AppConfig instance  = null;
	
	private Context context;
	
	private String apiBaseUrl;
	
	private String htmlPageUrl;
	
	private AppConfig() { }
	
	public static AppConfig getInstance() {
	      if (instance == null) {
	          synchronized (AppConfig.class) {
	              if (instance == null) {
	                  instance = new AppConfig();
	              }
	          }
	      }
		  return instance;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
    public String getHtmlPageUrl() {
    	return htmlPageUrl;
    }
    
    
    public void setHtmlPageUrl(String url) {
    	this.htmlPageUrl = url;
    }
    
    public String getAPIBaseUrl() {
    	return apiBaseUrl;
    }
    
    public void setAPIBaseUrl(String url) {
    	this.apiBaseUrl = url;
    }

	public String getSessionId() {		
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        String sessionId = prefs.getString(JsonUtility.SESSION_TOKEN_PARAM, null);
		return sessionId;
	}

	public void setSessionId(String sessionId) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(JsonUtility.SESSION_TOKEN_PARAM, sessionId);
        editor.commit();
	}

	public int getLastLevelPlayed() {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        int lastLevelPlayed = prefs.getInt(LAST_LEVEL_PLAYED_PARAM, 1);
		return lastLevelPlayed;
	}

	public void setLastLevelPlayed(int lastLevelPlayed) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(LAST_LEVEL_PLAYED_PARAM, lastLevelPlayed);
        editor.commit();		
	}
	
	public int getLastP4RCPoints() {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        int lastP4PRPoints = prefs.getInt(LAST_P4RC_POINTS_PARAM, 0);
		return lastP4PRPoints;
	}

	public void setLastP4RCPoints(int lastP4PRPoints) {
        final SharedPreferences prefs = context.getSharedPreferences(
                PREFERENCE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(LAST_P4RC_POINTS_PARAM, lastP4PRPoints);
        editor.commit();
	}
		
    public String getMacAddress(){
    	WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo wifiInf = wifiMan.getConnectionInfo();
    	String macAddr = wifiInf.getMacAddress();
    	return macAddr == null ? "3C:07:54:14:E9:F2" : macAddr;
    }
}
