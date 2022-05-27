package com.p4rc.sdk.task;

import java.util.HashMap;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;

public class PointsGameTask extends CustomAsyncTask<Object, String, Boolean> {

	public PointsGameTask(Context mContext) {
		super(mContext);
	}
	
	private HashMap<String, Object> responseData;

	@Override
	protected Boolean doInBackground(Object... params) {
		responseData = protocol.requestPointsTable();
		return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}
	
	public HashMap<String, Object> getData() {
		return responseData;
	}

}
