package com.p4rc.sdk.utils;

import android.util.Log;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.AuthSession;
import com.p4rc.sdk.model.GamePoint;
import com.p4rc.sdk.model.Point;
import com.p4rc.sdk.model.gamelist.Game;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.net.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonUtility {

	public static final String TAG = "JsonUtility";
	
	public static final String PAYLOAD_PARAM = "payload";
	public static final String PAYLOAD_FB_PARAM = "user";
	public static final String PASSWORD_PARAM = "password";
	public static final String AUTH_TYPE_PARAM = "authType";
	public static final String EMAIL_PARAM = "email";
	public static final String STATUS_PARAM = "status";
	public static final String USER_PARAM = "user";
	public static final String FIRST_NAME_PARAM = "firstName";
	public static final String LAST_NAME_PARAM = "lastName";
	public static final String USER_SOURCE_PARAM = "userSource";
	public static final String USER_TYPE_PARAM = "userType";
	public static final String USER_ACCEPTED_TERMS = "userAcceptedTerms";
	public static final String PROFILE_COMPLETION_PERCENT_PARAM = "profileCompletionPercent";
	public static final String USER_AVATAR_URL_PARAM = "userAvatarURL";
	public static final String TOTAL_POINTS_PARAM = "totalPoints";
	public static final String FACEBOOK_ID_PARAM = "facebookId";
	public static final String FB_PUBLISH_ALLOWED_PARAM = "fbPublishAllowed";
	public static final String ID_PARAM = "id";
	public static final String SESSION_TOKEN_PARAM = "sessionToken";
	public static final String AUTH_STATUS_PARAM = "authStatus";
	public static final String GAME_REF_ID = "gameRefId";
	public static final String FB_ACCESS_TOKEN = "fbAccessToken";
	public static final String EXPIRY_TIME_PARAM = "expiryTime";
	public static final String DEVICE_TYPE_PARAM = "deviceType";
	public static final String MESSAGE_PARAM = "message";
	public static final String CODE_PARAM = "code";
	public static final String FB_EMAIL_PARAM = "fb_e_mail";
	public static final String FB_FIRST_NAME_PARAM = "fb_first_name";
	public static final String FB_LAST_NAME_PARAM = "fb_last_name";
	public static final String SECURITY_OBJECT_PARAM = "securityObject";
	public static final String SESSION_ID_PARAM = "sessionId";
	public static final String INSTANCE_ID_PARAM = "instanceId";
	public static final String START_TIME_PARAM = "startTimeInGMT";
	public static final String END_TIME_PARAM = "endTimeInGMT";
	public static final String GAME_POINTS_PARAM = "gamePoints";
	public static final String MINUTES_PLAYED_PARAM = "minutesPlayed";
	public static final String LEVEL_PARAM = "level";
	public static final String P4RC_POINTS = "p4rcPoints";
	public static final String GAME_SCORE_PARAM = "gameScore";
	public static final String TRANS_TIME_PARAM = "transTime";
	public static final String CACHE_PARAM = "cache";
	public static final String POINTS_TABLE_PARAM = "PointsTable";
	public static final String OPERATION_SYSTEM_PARAM = "operatingSystem";
	public static final String SDK_VERSION_PARAM = "sdkVersion";
	
	
	public static final String SUCCESS_STATUS = "SUCCESS";
	public static final String SUCCESS_ERROR = "ERROR";
	
	//error codes for parsing/creating JSON objects
	public static final int NO_ERROR = 0;
	public static final int CONSTRUCTING_PARAMS_ERROR = 1;
	public static final int GETTING_PARAMS_ERROR = 2;
	private static final String DOB = "dob";


	//constants for requests
	private final String AUTH_TYPE_REGULAR = "REGULAR";
	
	private int lastErorCode = 0;
	
	
	public int getLastErrorCode() {
		return lastErorCode;
	}
	
	public String getSignInRequestParams(String password, String email) {
		JSONObject parameters = new JSONObject();
		JSONObject innerParams = new JSONObject();
		
		try {
			innerParams.put(AUTH_TYPE_PARAM, AUTH_TYPE_REGULAR);
			innerParams.put(PASSWORD_PARAM, password);
			innerParams.put(EMAIL_PARAM, email);
			parameters.put(PAYLOAD_FB_PARAM, innerParams);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return parameters.toString();
	}
	
	
	public String getSignUpParams(String deviceType, String firstName, String lastName,
			boolean isUserAcceptedTerms, String password, String email, String gameRefId) {
		JSONObject payload = new JSONObject();
		JSONObject innerParams = new JSONObject();
		try {
			innerParams.put(DEVICE_TYPE_PARAM, deviceType);
			innerParams.put(FIRST_NAME_PARAM, firstName);
			innerParams.put(LAST_NAME_PARAM, lastName);
			innerParams.put(USER_ACCEPTED_TERMS, isUserAcceptedTerms);
			innerParams.put(PASSWORD_PARAM, password);
			innerParams.put(EMAIL_PARAM, email);
			innerParams.put(GAME_REF_ID, gameRefId);
			payload.put(PAYLOAD_PARAM, innerParams);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return payload.toString();
	}

	public String getSignUpNewParams(String firstName, String lastName, String email, String password, String deviceType, String dob) {
		JSONObject payload = new JSONObject();
		JSONObject innerParams = new JSONObject();
		try {
			innerParams.put(FIRST_NAME_PARAM, firstName);
			innerParams.put(LAST_NAME_PARAM, lastName);
			innerParams.put(EMAIL_PARAM, email);
			innerParams.put(PASSWORD_PARAM, password);
			innerParams.put(DEVICE_TYPE_PARAM, deviceType);
			innerParams.put(DOB, dob);
			payload.put(PAYLOAD_PARAM, innerParams);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return payload.toString();
	}

	public String getSignUpParamsWithFBAccessToken(String fbAccessToken) {
		JSONObject payload = new JSONObject();
		JSONObject innerParams = new JSONObject();
		try {
			innerParams.put(AUTH_TYPE_PARAM, "FACEBOOK");
			innerParams.put(FB_ACCESS_TOKEN, fbAccessToken);
			payload.put(PAYLOAD_FB_PARAM, innerParams);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return payload.toString();
	}
	
	public String getPasswordResetParams(String email) {
		JSONObject params = new JSONObject();
		JSONObject payload = new JSONObject();
		try {
			payload.put(EMAIL_PARAM, email);
			params.put(PAYLOAD_PARAM, payload);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
	
	public String getPlayerPingParams(boolean isLoggedIn) {
		JSONObject params = new JSONObject();
		try {
			JSONObject payload = new JSONObject();			
			payload.put(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			payload.put(INSTANCE_ID_PARAM, AppConfig.getInstance().getMacAddress());
			payload.put(OPERATION_SYSTEM_PARAM, Constants.OPERATION_SYSTEM);
			payload.put(SDK_VERSION_PARAM, Constants.SDK_VERSION);
			
			if(isLoggedIn) {
				JSONObject securityObject = new JSONObject();
				securityObject.put(SESSION_ID_PARAM, AppConfig.getInstance().getSessionId());
				params.put(SECURITY_OBJECT_PARAM, securityObject);
			}

			params.put(PAYLOAD_PARAM, payload);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
	
	public String getCheckInPointsParams(int level) {
		JSONObject params = new JSONObject();
		JSONObject securityObject = new JSONObject();
		JSONObject payload = new JSONObject();
		try {
			securityObject.put(SESSION_ID_PARAM, AppConfig.getInstance()
					.getSessionId());
			payload.put(START_TIME_PARAM, AppUtils.
					getFormatedTimeString(PointsManager.getInstance(AppConfig.getInstance().getContext()).
							getGamePoint(level).getStartTime()));
			payload.put(END_TIME_PARAM, AppUtils.getFormatedTimeString(PointsManager.
					getInstance(AppConfig.getInstance().getContext()).getGamePoint(level).getEndTime()));
 			payload.put(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			payload.put(GAME_POINTS_PARAM, PointsManager.getInstance(AppConfig.getInstance().getContext()).
					getGamePoint(level).getGamePoints());
			payload.put(MINUTES_PLAYED_PARAM, PointsManager.getInstance(AppConfig.getInstance().getContext()).
					getGamePoint(level).getMinutesPlayed());
			payload.put(LEVEL_PARAM, level);

 			params.put(SECURITY_OBJECT_PARAM, securityObject);
 			params.put(PAYLOAD_PARAM, payload);

		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
	
	public String getPointsTableParams() {
		JSONObject params = new JSONObject();
		JSONObject payload = new JSONObject();
		try {
			payload.put(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			params.put(PAYLOAD_PARAM, payload);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
	
	public String getConvertPointsParams(long gamePoints,
			int level, int minutesPlayed, boolean isLoggedIn) {
		JSONObject params = new JSONObject();
		JSONObject securityObject = new JSONObject();
		JSONObject payload = new JSONObject();
		
		
		try {
			payload.put(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			payload.put(GAME_POINTS_PARAM, gamePoints);
			payload.put(MINUTES_PLAYED_PARAM, minutesPlayed);
			payload.put(LEVEL_PARAM, level);
			
			if(isLoggedIn) {
				securityObject.put(SESSION_ID_PARAM, AppConfig.getInstance().getSessionId());
				params.put(SECURITY_OBJECT_PARAM, securityObject);
			}
			
			params.put(PAYLOAD_PARAM, payload);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
	
	public String getPointsBatchParams() {
		JSONObject parameters = new JSONObject();
		JSONObject securityObject = new JSONObject();
		JSONObject payload = new JSONObject();
		JSONArray cache = new JSONArray();
		
		try {
			securityObject.putOpt(SESSION_ID_PARAM, AppConfig.getInstance().
					getSessionId());
			payload.putOpt(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			ArrayList<GamePoint> pointsTable = P4RC.getInstance().getPointsManager().getGamePointsTable();
			for (GamePoint gamePoint : pointsTable) {
				JSONObject cacheEntry = new JSONObject();
				cacheEntry.putOpt(END_TIME_PARAM, AppUtils.getFormatedTimeString(gamePoint.getEndTime()));
				cacheEntry.putOpt(TRANS_TIME_PARAM, AppUtils.getTransTime(gamePoint.getTransTime()));
				cacheEntry.put(GAME_POINTS_PARAM, String.valueOf(gamePoint.getGamePoints()));
				cacheEntry.put(MINUTES_PLAYED_PARAM, String.valueOf(gamePoint.getMinutesPlayed()));
				cacheEntry.putOpt(START_TIME_PARAM, AppUtils.getFormatedTimeString(gamePoint.getStartTime()));
				cacheEntry.put(LEVEL_PARAM, String.valueOf(gamePoint.getLevel()));
				cache.put(cacheEntry);
			}
			payload.putOpt(CACHE_PARAM, cache);
			parameters.putOpt(SECURITY_OBJECT_PARAM, securityObject);
			parameters.putOpt(PAYLOAD_PARAM, payload);
			
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return parameters.toString();
	}
	
	
	public String getMaxPointParams() {
		JSONObject params = new JSONObject();
		JSONObject payload = new JSONObject();
		try {
			payload.put(GAME_REF_ID, P4RC.getInstance().getGameRefId());
			params.put(PAYLOAD_PARAM, payload);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();
	}
//	created on 01/06/2022
	public  Response<GameList> encodeGameListResponse(String responseString) {
		if (responseString == null){
			return new Response<>(lastErorCode, "No response from server", null);
		}
		try {
			JSONObject response = new JSONObject(responseString);

			String status = response.getString(STATUS_PARAM);
			JSONObject payload = response.getJSONObject(PAYLOAD_PARAM);
			if (status.equals(SUCCESS_STATUS)) {
				return new Response<>(200, "Success", GameList.fromJSON(payload));
			} else {
				return new Response<>(payload.optInt(CODE_PARAM), payload.optString(MESSAGE_PARAM), null);
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
			return new Response<>(888, "Response Not In Json", null);
		}
	}

	public Response<AuthSession> encodeLoginResponse(String responseString) {
		Log.d(TAG, "encodeLoginResponse: " + responseString);
		if (responseString == null){
			return new Response<>(lastErorCode, "No response from server", null);
		}
		try {
			JSONObject response = new JSONObject(responseString);

			String status = response.getString(STATUS_PARAM);
			JSONObject payload = response.getJSONObject(PAYLOAD_PARAM);
			if (status.equals(SUCCESS_STATUS)) {
				return new Response<>(200, "Success", AuthSession.fromJSON(payload));
			} else {
				return new Response<>(payload.optInt(CODE_PARAM), payload.optString(MESSAGE_PARAM), null);
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
			return new Response<>(888, "Response Not In Json", null);
		}
	}
	
	public HashMap<String, Object> encodeSignUpResponse(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			if(response != null) {
				String status = response.optString(STATUS_PARAM);
				JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
				data.put(STATUS_PARAM, status);
				if (status.equals(SUCCESS_STATUS)) {
					data.put(EMAIL_PARAM, payload.optString(EMAIL_PARAM));
					data.put(LAST_NAME_PARAM, payload.optString(LAST_NAME_PARAM));
					data.put(FIRST_NAME_PARAM, payload.optString(FIRST_NAME_PARAM));
					data.put(PROFILE_COMPLETION_PERCENT_PARAM, 
							payload.optDouble(PROFILE_COMPLETION_PERCENT_PARAM));
					data.put(USER_AVATAR_URL_PARAM, payload.optString(USER_AVATAR_URL_PARAM));
					data.put(TOTAL_POINTS_PARAM, payload.optLong(TOTAL_POINTS_PARAM));
					data.put(FACEBOOK_ID_PARAM, payload.optString(FACEBOOK_ID_PARAM));
					data.put(ID_PARAM, payload.optInt(ID_PARAM));
				} else if (status.equals(SUCCESS_ERROR)) {
					data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
					data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
				}
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}


	
	public HashMap<String, Object> encodeSignInUserWithFBAccessTokenResponse(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			if(response != null) {
				String status = response.optString(STATUS_PARAM);
				data.put(STATUS_PARAM, status);
				JSONObject payload = response.getJSONObject(PAYLOAD_PARAM);
				if (status.equals(SUCCESS_STATUS)){
					JSONObject user = payload.getJSONObject(USER_PARAM);
					data.put(FB_EMAIL_PARAM, user.optString(FB_EMAIL_PARAM));
					data.put(FB_FIRST_NAME_PARAM, user.optString(FB_FIRST_NAME_PARAM));
					data.put(FB_LAST_NAME_PARAM, user.optString(FB_LAST_NAME_PARAM));
					data.put(USER_SOURCE_PARAM, user.optInt(USER_SOURCE_PARAM));
					data.put(USER_TYPE_PARAM, user.optInt(USER_TYPE_PARAM));
					data.put(USER_ACCEPTED_TERMS, user.optBoolean(USER_ACCEPTED_TERMS));
					data.put(PROFILE_COMPLETION_PERCENT_PARAM, user.optInt(PROFILE_COMPLETION_PERCENT_PARAM));
					data.put(USER_AVATAR_URL_PARAM, user.optString(USER_AVATAR_URL_PARAM));
					data.put(TOTAL_POINTS_PARAM, user.optInt(TOTAL_POINTS_PARAM));
					data.put(FACEBOOK_ID_PARAM, user.optString(FACEBOOK_ID_PARAM));
					data.put(FB_PUBLISH_ALLOWED_PARAM, user.optBoolean(FB_PUBLISH_ALLOWED_PARAM));
					data.put(ID_PARAM, user.optInt(ID_PARAM));

					data.put(SESSION_TOKEN_PARAM, payload.optString(SESSION_TOKEN_PARAM));
					data.put(AUTH_STATUS_PARAM, payload.optString(AUTH_STATUS_PARAM));
				} else if (status.equals(SUCCESS_ERROR)){
					data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
					data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
				}
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}

	
	public HashMap<String, Object> encodePasswordResetResponse(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {
				data.put(PAYLOAD_PARAM, true);
			} else if(SUCCESS_ERROR.equals(status)) {
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}


	public HashMap<String, Object> encodePlayerPing(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {
				data.put(PAYLOAD_PARAM, true);
			} else if(SUCCESS_ERROR.equals(status)) {
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}
	
	public HashMap<String, Object> encodeCheckinPoints(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {
				data.put(P4RC_POINTS, payload.optInt(P4RC_POINTS));
			} else if(SUCCESS_ERROR.equals(status)) {
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}

	public HashMap<String, Object> encodePointsTable(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {	
				Point point = null;
				Map<Integer, Point> pointsTable = new HashMap<Integer, Point>();
				JSONArray payload = response.optJSONArray(PAYLOAD_PARAM);
				for (int i = 0; i < payload.length(); i++) {
					JSONObject row = payload.getJSONObject(i);
					point = Point.parse(row);
					Log.d(TAG, point.toString());
					pointsTable.put(point.getLevel(), point);
				}
				data.put(POINTS_TABLE_PARAM, pointsTable);
			} else if(SUCCESS_ERROR.equals(status)) {
				JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}

	public HashMap<String, Object> encodeConvertPoints(String responseString){
			HashMap<String, Object> data = new HashMap<String, Object>();
			if (responseString == null){
				return data;
			}
			JSONObject response = null;
			try {
				response = new JSONObject(responseString);
				String status = response.optString(STATUS_PARAM);
				Log.v("", "encodeConvertPoints ----> status=" + status);
				JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
				data.put(STATUS_PARAM, status);
				if(SUCCESS_STATUS.equals(status)) {
					data.put(P4RC_POINTS, payload.optInt(P4RC_POINTS));
				} else if(SUCCESS_ERROR.equals(status)) {
					data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
					data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
				}
			} catch (JSONException e) {
				lastErorCode = GETTING_PARAMS_ERROR;
			}
			return data;
	}
	
	public HashMap<String, Object> encodeBatchPointsResponse(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {
				data.put(P4RC_POINTS, payload.optInt(P4RC_POINTS));
			} else if(SUCCESS_ERROR.equals(status)) {
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}

	public HashMap<String, Object> encodeMaxPoints(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			String status = response.optString(STATUS_PARAM);
			JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
			data.put(STATUS_PARAM, status);
			if(SUCCESS_STATUS.equals(status)) {
				data.put(P4RC_POINTS, payload.optInt(P4RC_POINTS));
			} else if(SUCCESS_ERROR.equals(status)) {
				data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
				data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}

	public String getUserInfoParams() {
		JSONObject params = new JSONObject();
		JSONObject securityObject = new JSONObject();
		try {
			securityObject.put(SESSION_ID_PARAM, AppConfig.getInstance()
					.getSessionId());
			params.put(SECURITY_OBJECT_PARAM, securityObject);
		} catch (JSONException e) {
			lastErorCode = CONSTRUCTING_PARAMS_ERROR;
		}
		return params.toString();

	}

	public HashMap<String, Object> encodeUserInfo(String responseString) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (responseString == null){
			return data;
		}
		JSONObject response = null;
		try {
			response = new JSONObject(responseString);
			if(response != null) {
				String status = response.optString(STATUS_PARAM);
				JSONObject payload = response.optJSONObject(PAYLOAD_PARAM);
				JSONObject userData = payload.optJSONObject(USER_PARAM);
				data.put(STATUS_PARAM, status);
				if (status.equals(SUCCESS_STATUS)) {
					data.put(EMAIL_PARAM, userData.optString(EMAIL_PARAM));
					data.put(FIRST_NAME_PARAM, userData.optString(FIRST_NAME_PARAM));
					data.put(LAST_NAME_PARAM, userData.optString(LAST_NAME_PARAM));
					data.put(PROFILE_COMPLETION_PERCENT_PARAM, 
							userData.optDouble(PROFILE_COMPLETION_PERCENT_PARAM));
					data.put(USER_AVATAR_URL_PARAM, userData.optString(USER_AVATAR_URL_PARAM));
					data.put(TOTAL_POINTS_PARAM, userData.optInt(TOTAL_POINTS_PARAM));
					data.put(FACEBOOK_ID_PARAM, userData.optString(FACEBOOK_ID_PARAM));
					data.put(ID_PARAM, userData.optLong(ID_PARAM));
				} else if (status.equals(SUCCESS_ERROR)) {
					data.put(MESSAGE_PARAM, payload.optString(MESSAGE_PARAM));
					data.put(CODE_PARAM, payload.optInt(CODE_PARAM));
				}
			}
		} catch (JSONException e) {
			lastErorCode = GETTING_PARAMS_ERROR;
		}
		return data;
	}
}
