package com.p4rc.sdk.task;

import java.util.HashMap;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

public class GetUserInfoTask  extends CustomAsyncTask<Object, String, Boolean> {

	public GetUserInfoTask(Context mContext) {
		super(mContext);
	}
	
	public GetUserInfoTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private HashMap<String, Object> responseData;

	@Override
	protected Boolean doInBackground(Object... args) {
		responseData = protocol.requestUserInfo();
		return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(JsonUtility.SUCCESS_STATUS.equals(responseData.get(JsonUtility.STATUS_PARAM))) {
			int points = (Integer)responseData.get(JsonUtility.TOTAL_POINTS_PARAM);
			PointsManager.getInstance(mContext).setTotalP4RCPoints(points);
		}
		super.onPostExecute(result);
	}
	
	public HashMap<String, Object> getData() {
		return responseData;
	}

}
