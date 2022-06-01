package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.net.Response;
import com.p4rc.sdk.utils.JsonUtility;

import java.util.HashMap;

public class GameListTask extends CustomAsyncTask<Object, String, Boolean> {

	public GameListTask(Context mContext) {
		super(mContext);
	}

	public GameListTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private Response<GameList> responseData;

	@Override
	protected Boolean doInBackground(Object... args) {
		responseData = protocol.requestGameList();
		return responseData != null && responseData.getCode() == 200;
	}
	
	public Response<GameList> getData() {
		return responseData;
	}
}
