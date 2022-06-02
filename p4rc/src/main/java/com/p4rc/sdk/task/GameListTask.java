package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.net.Response;
import com.p4rc.sdk.utils.JsonUtility;

import java.util.HashMap;

public class GameListTask extends CustomAsyncTask<Object, String, Response<GameList>> {

	public GameListTask(Context mContext) {
		super(mContext);
	}

	public GameListTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private Response<GameList> responseData;

	@Override
	protected Response<GameList> doInBackground(Object... args) {
		responseData = protocol.requestGameList();
		return responseData;
	}

	@Override
	protected void onPostExecute(Response<GameList> aBoolean) {
		super.onPostExecute(aBoolean);
	}

	public Response<GameList> getData() {
		return responseData;
	}
}
