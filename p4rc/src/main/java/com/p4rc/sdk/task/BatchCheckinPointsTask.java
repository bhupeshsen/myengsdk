package com.p4rc.sdk.task;

import java.util.HashMap;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

public class BatchCheckinPointsTask extends CustomAsyncTask<Object, String, Boolean> {

	private HashMap<String, Object> data;
	
	public BatchCheckinPointsTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}

	@Override
	protected Boolean doInBackground(Object... params) {
		data = protocol.requestBatchPoints();
		return data == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(data.get(JsonUtility.STATUS_PARAM));
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(JsonUtility.SUCCESS_STATUS.equals(data.get(JsonUtility.STATUS_PARAM))) {
			int points = (Integer)data.get(JsonUtility.P4RC_POINTS);
			PointsManager.getInstance(mContext).setTotalP4RCPoints(points);
		} else {
			//PointsManager.getInstance(mContext).setLastP4RCPoints(0);
		}
		super.onPostExecute(result);
	}

	public HashMap<String, Object> getData() {
		return data;
	}
}
