package com.p4rc.sdk.task;

import java.util.HashMap;

import com.p4rc.sdk.utils.JsonUtility;

import android.content.Context;

public class MaxPointsTask extends CustomAsyncTask<Object, String, Boolean> {

	public MaxPointsTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}

	private HashMap<String, Object> responseData;
	
	@Override
	protected Boolean doInBackground(Object... params) {
		responseData = protocol.requestMaxPoints();
		return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}

	public HashMap<String, Object> getData() {
		return responseData;
	}
}
