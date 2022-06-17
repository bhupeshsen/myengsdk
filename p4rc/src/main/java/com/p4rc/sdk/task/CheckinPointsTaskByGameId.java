package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

import java.util.HashMap;

public class CheckinPointsTaskByGameId extends CustomAsyncTask<Object, String, Boolean> {

	public CheckinPointsTaskByGameId(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}

	private HashMap<String, Object> responseData;
	
	@Override
	protected Boolean doInBackground(Object... params) {
		responseData = protocol.requestCheckInPointsByGameId((Integer)params[0], (Integer)params[1],(String) params[2],(Long) params[3]);
		return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(JsonUtility.SUCCESS_STATUS.equals(responseData.get(JsonUtility.STATUS_PARAM))) {
			int points = (Integer)responseData.get(JsonUtility.P4RC_POINTS);
			PointsManager.getInstance(mContext).setTotalP4RCPoints(points);
		} else {
			//PointsManager.getInstance(mContext).setLastP4RCPoints(0);
		}

		super.onPostExecute(result);
	}

	public HashMap<String, Object> getData() {
		return responseData;
	}
}
