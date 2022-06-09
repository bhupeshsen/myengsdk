package com.p4rc.sdk.net;

import android.util.Log;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.AuthSession;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.utils.JsonUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConnectionProtocol extends ConnectionClient {

    private static final String GAME_LIST_REQUEST_METHOD = "v1/games?deviceName=null";
    /**
     * parameters for every request to the server
     */
    private String BASE_URL = "";
    private static final String SIGN_UP_REQUEST_METHOD = "v1/user/registerNewUser";
    private static final String SIGN_UP_NEW_REQUEST_METHOD = "v1/users/registerNewUserNLogin";
    private static final String LOGIN_REQUEST_METHOD = "v1/users/authenticate";
    private static final String PASSWORD_RESET_REQUEST_METHOD = "v1/user/forgotPassword";
    private static final String PALYER_PING_REQUEST_METHOD = "v1/user/playerPing";
    private static final String CHECKIN_POINTS_METHOD = "v2/userpoints/checkinPoints";
    private static final String POINTS_TABLE_METHOD = "v1/gamepoints/pointsTable";
    private static final String CONVERT_POINTS_METHOD = "v1/gamepoints/convertPoints";
    private static final String BATCH_POINTS_METHOD = "v2/userpoints/checkinPointsInBatch";
    private static final String MAX_POINTS_METHOD = "v1/gamepoints/maxPoints";
    private static final String USER_INFO_METHOD = "v1/user/userInfo";

    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String GET_METHOD = "GET";

    public static final String AUTH_TYPE_REGULAR = "REGULAR";
    public static final String AUTH_TYPE_FACEBOOK = "FACEBOOK";

    public static final String API_KEY_PARAM = "X-MYXR-ApiKey";

    public static final String DEVICE_TYPE_MOBILE = "mobile";
    public static final String DEVICE_TYPE_SDK = "sdk";

    private static ConnectionProtocol instance;

    private JsonUtility jsonUtility;

    private ConnectionProtocol() {
        jsonUtility = new JsonUtility();
        BASE_URL = AppConfig.getInstance().getAPIBaseUrl();
    }

    private ConnectionProtocol(String baseUrl) {
        BASE_URL = baseUrl;
        jsonUtility = new JsonUtility();
    }

    public static ConnectionProtocol getInstance(String baseUrl) {
        if (instance == null) {
            if (baseUrl != null) {
                instance = new ConnectionProtocol(baseUrl);
            } else {
                instance = new ConnectionProtocol();
            }
        }
        return instance;
    }

    private String completeURL(String baseURL, String params) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseURL);
        builder.append(params);
        return builder.toString();
    }

    public Response<AuthSession> requestLogin(String password, String email) {
        String url = completeURL(BASE_URL, LOGIN_REQUEST_METHOD);
        String json = jsonUtility.getSignInRequestParams(password, email);
        return jsonUtility.encodeLoginResponse(super.makeRequestToServer(url, POST_METHOD, json));
    }

    public HashMap<String, Object> requestSignUp(String deviceType, String firstName, String lastName,
                                                 boolean isUserAcceptedTerms, String password, String email, String gameRefId) {
        String url = completeURL(BASE_URL, SIGN_UP_REQUEST_METHOD);
        String json = jsonUtility.getSignUpParams(deviceType, firstName, lastName,
                isUserAcceptedTerms, password, email, gameRefId);
        return jsonUtility.encodeSignUpResponse(super.makeRequestToServer(url, PUT_METHOD, json));
    }

    public Response<AuthSession> requestSignUpNew(
            String firstName, String lastName,String email, String password, String deviceType,  String dob) {
        String url = completeURL(BASE_URL, SIGN_UP_NEW_REQUEST_METHOD);
        String json = jsonUtility.getSignUpNewParams(firstName, lastName, email, password, deviceType, dob);
        Log.d(TAG, "requestSignUpNew: " + json);
        return jsonUtility.encodeLoginResponse(super.makeRequestToServer(url, POST_METHOD, json));
    }

    public Response<GameList> requestGameList() {
        String url = completeURL(BASE_URL, GAME_LIST_REQUEST_METHOD);
//        fixme myxr-api-key is manually added
        Map<String,String> headers = new HashMap<>();
        headers.put("X-MYXR-ApiKey", "f3ba3335-c475-4c93-870d-cc33e423dd31");
        JSONObject params = new JSONObject();
        try {
            params.put("campaignType", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response = super.makeRequestToServer(url, GET_METHOD, params.toString(), headers);
//        Log.d(TAG, "requestGameList: " + response);
        return jsonUtility.encodeGameListResponse(response);
    }

    public HashMap<String, Object> requestPasswordReset(String email) {
        String url = completeURL(BASE_URL, PASSWORD_RESET_REQUEST_METHOD);
        String json = jsonUtility.getPasswordResetParams(email);
        return jsonUtility.encodePasswordResetResponse(super.makeRequestToServer(url, POST_METHOD, json));
    }

    public HashMap<String, Object> requestSignInUserWithFBAccessToken(String fbAccessToken) {
        String url = completeURL(BASE_URL, LOGIN_REQUEST_METHOD);
        String json = jsonUtility.getSignUpParamsWithFBAccessToken(fbAccessToken);
        return jsonUtility.encodeSignInUserWithFBAccessTokenResponse(super.makeRequestToServer(url, POST_METHOD, json));
    }

    public HashMap<String, Object> requestPlayerPing(boolean isLoggedIn) {
        String url = completeURL(BASE_URL, PALYER_PING_REQUEST_METHOD);
        String params = jsonUtility.getPlayerPingParams(isLoggedIn);
        return jsonUtility.encodePlayerPing(super.makeRequestToServer(url, POST_METHOD, params));
    }

    /**
     * Sends user game points to the P4RC server. Don't forget to add
     * at least one finished game level with earned points.
     *
     * @param level finished level
     * @return server response with data about points calculation
     */
    public HashMap<String, Object> requestCheckInPoints(int level, int levelPoints) {
        String url = completeURL(BASE_URL, CHECKIN_POINTS_METHOD);
        Map<String, String> headers = new HashMap<>();
        headers.put(API_KEY_PARAM, P4RC.getInstance().getApiKey());
        String params = jsonUtility.getCheckInPointsParams(level);
        return jsonUtility.encodeCheckinPoints(super.makeRequestToServer(url, PUT_METHOD, params, headers));
    }

    public HashMap<String, Object> requestPointsTable() {
        String url = completeURL(BASE_URL, POINTS_TABLE_METHOD);
        String params = jsonUtility.getPointsTableParams();
        return jsonUtility.encodePointsTable(super.makeRequestToServer(url, POST_METHOD, params));
    }

    public HashMap<String, Object> requestConvertPoints(int gamePoints, int level,
                                                        int minutesPlayed, boolean isLoggedIn) {
        String url = completeURL(BASE_URL, CONVERT_POINTS_METHOD);
        String params = jsonUtility.getConvertPointsParams(gamePoints, level,
                    minutesPlayed, isLoggedIn);
        return jsonUtility.encodeConvertPoints(super.makeRequestToServer(url, PUT_METHOD, params));
    }

    /**
     * Sends all accumulated game points to the server.
     * Important! Don't forget to add game points for levels before this method
     * calling.
     *
     * @return Map with server response data.
     */
    public HashMap<String, Object> requestBatchPoints() {
        String url = completeURL(BASE_URL, BATCH_POINTS_METHOD);
        String params = jsonUtility.getPointsBatchParams();
       	Map<String, String> headers = new HashMap<>();
		headers.put(API_KEY_PARAM, P4RC.getInstance().getApiKey());
		return jsonUtility.encodeBatchPointsResponse(super.makeRequestToServer(url, PUT_METHOD, params, headers));
    }

    public HashMap<String, Object> requestMaxPoints() {
        String url = completeURL(BASE_URL, MAX_POINTS_METHOD);
        String params = jsonUtility.getMaxPointParams();
        return jsonUtility.encodeMaxPoints(super.makeRequestToServer(url, POST_METHOD, params));
    }

    public HashMap<String, Object> requestUserInfo() {
        String url = completeURL(BASE_URL, USER_INFO_METHOD);
        String params = jsonUtility.getUserInfoParams();
        return jsonUtility.encodeUserInfo(super.makeRequestToServer(url, POST_METHOD, params));
    }
}
