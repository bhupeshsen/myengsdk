package com.p4rc.sdk.task;

import java.util.HashMap;

import android.content.Context;

import com.p4rc.sdk.utils.JsonUtility;

public class SignUpTask extends CustomAsyncTask<Object, String, Boolean> {

	public SignUpTask(Context mContext) {
		super(mContext);
	}
	
	public SignUpTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private HashMap<String, Object> responseData;

	@Override
	protected Boolean doInBackground(Object... args) {
		responseData = protocol.requestSignUp((String)args[0], (String)args[1], (String)args[2],
				(Boolean)args[3], (String)args[4], (String)args[5], (String)args[6]);
		return responseData == null ? false : JsonUtility.SUCCESS_STATUS.
				equals(responseData.get(JsonUtility.STATUS_PARAM));
	}
	
	public HashMap<String, Object> getData() {
		return responseData;
	}
}
