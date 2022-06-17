package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.model.GamePoint;
import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.model.gamelist.GamePoints;
import com.p4rc.sdk.net.Response;

public class GamePointsTask extends CustomAsyncTask<Object, String, Response<GamePoints>> {

	public GamePointsTask(Context mContext) {
		super(mContext);
	}

	public GamePointsTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private Response<GamePoints> responseData;

	@Override
	protected Response<GamePoints> doInBackground(Object... args) {
		responseData = protocol.getPoints((String) args[0]);
		return responseData;
	}

	@Override
	protected void onPostExecute(Response<GamePoints> aBoolean) {
		super.onPostExecute(aBoolean);
	}

	public Response<GamePoints> getData() {
		return responseData;
	}
}
