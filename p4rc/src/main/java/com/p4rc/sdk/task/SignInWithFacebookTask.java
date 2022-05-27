package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

import java.util.HashMap;

public class SignInWithFacebookTask extends CustomAsyncTask<Object, String, Boolean> {

    public SignInWithFacebookTask(Context mContext) {
        super(mContext);
    }

    public SignInWithFacebookTask(Context mContext, boolean showProgress) {
        super(mContext, showProgress);
    }

    private HashMap<String, Object> responseData;

    @Override
    protected Boolean doInBackground(Object... args) {
        responseData = protocol.requestSignInUserWithFBAccessToken((String) args[0]);
        return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
                equals(responseData.get(JsonUtility.STATUS_PARAM));
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (JsonUtility.SUCCESS_STATUS.equals(responseData.get(JsonUtility.STATUS_PARAM))) {
            AppConfig.getInstance().setSessionId(
                    (String) responseData.get(JsonUtility.SESSION_TOKEN_PARAM));

            int points = (Integer) responseData.get(JsonUtility.TOTAL_POINTS_PARAM);
            PointsManager.getInstance(mContext).setTotalP4RCPoints(points);
        }
        super.onPostExecute(result);
    }

    public HashMap<String, Object> getData() {
        return responseData;
    }

}
