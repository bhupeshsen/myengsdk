package com.p4rc.sdk.task;

import java.util.HashMap;

import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

import android.content.Context;

public class CheckinPointsTask extends CustomAsyncTask<Object, String, Boolean> {

	public CheckinPointsTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}

	private HashMap<String, Object> responseData;
	
	@Override
	protected Boolean doInBackground(Object... params) {
		responseData = protocol.requestCheckInPoints((Integer)params[0], (Integer)params[1]);
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
