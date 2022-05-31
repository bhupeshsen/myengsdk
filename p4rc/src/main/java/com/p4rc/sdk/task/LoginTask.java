package com.p4rc.sdk.task;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.model.AuthSession;
import com.p4rc.sdk.model.GamePoint;
import com.p4rc.sdk.model.User;
import com.p4rc.sdk.net.Response;
import com.p4rc.sdk.utils.AppUtils;
import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;

import org.json.JSONObject;

public class LoginTask extends CustomAsyncTask<Object, String, Boolean> {

	public LoginTask(Context mContext) {
		super(mContext);
	}

	public LoginTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}

	private Response<AuthSession> responseData;

	@Override
	protected Boolean doInBackground(Object... args) {
		responseData = protocol
				.requestLogin((String) args[1], (String) args[0]);

		Log.d("TAG", "doInBackground: " + responseData.toString());
		return responseData != null && responseData.getCode() == 200;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
//			todo this is already going to save in user model
			int points = responseData.getData().getUser().getTotalPoints();
			PointsManager.getInstance(mContext).setTotalP4RCPoints(points);

			AppConfig.getInstance().setSessionId(responseData.getData().getSessionToken());

			AppConfig.getInstance().saveUser(responseData.getData().getUser());

			// send game points after off line mode
			final ArrayList<GamePoint> gamePointsMap = PointsManager.getInstance(AppConfig.getInstance().getContext()).getGamePointsTable();

			if (gamePointsMap != null && gamePointsMap.size() > 0) {
				if (AppUtils.isOnline(mContext)) {
					BatchCheckinPointsTask batchTask = new BatchCheckinPointsTask(
							mContext, false);
					batchTask.setAsyncTaskListener(new BatchTaskListener());
					batchTask.execute();
				}
			}

		}
		super.onPostExecute(result);
	}

	public Response<AuthSession> getData() {
		return responseData;
	}

	private class BatchTaskListener implements
			CustomAsyncTask.AsyncTaskListener {

		@Override
		public void onBeforeTaskStarted(CustomAsyncTask<?, ?, ?> task) {}

		@Override
		public void onTaskFinished(CustomAsyncTask<?, ?, ?> task) {
			HashMap<String, Object> responseData;
			if (task != null) {
				responseData = ((BatchCheckinPointsTask) task).getData();
				if (JsonUtility.SUCCESS_STATUS.equals(responseData
						.get(JsonUtility.STATUS_PARAM))) {
					PointsManager
							.getInstance(AppConfig.getInstance().getContext())
							.getGamePointsTable().clear();
					PointsManager.getInstance(
							AppConfig.getInstance().getContext())
							.saveGamePoints();
				}
			}
		}
	}
}
