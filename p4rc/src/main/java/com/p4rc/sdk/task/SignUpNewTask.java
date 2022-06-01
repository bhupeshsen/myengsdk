package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;

import java.util.HashMap;

public class SignUpNewTask extends CustomAsyncTask<Object, String, Boolean> {

	public SignUpNewTask(Context mContext) {
		super(mContext);
	}

	public SignUpNewTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private HashMap<String, Object> responseData;

	@Override
	protected Boolean doInBackground(Object... args) {
		responseData = protocol.requestSignUpNew((String)args[0], (String)args[1], (String)args[2],
				(String) args[3], (String)args[4], (String)args[5]);
		return responseData != null && JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}
	
	public HashMap<String, Object> getData() {
		return responseData;
	}
}
