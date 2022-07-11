package com.p4rc.sdk.task;

import android.content.Context;

import com.p4rc.sdk.model.CompanyDetails;
import com.p4rc.sdk.model.gamelist.GamePoints;
import com.p4rc.sdk.net.Response;

public class CompanyDetailsTask extends CustomAsyncTask<Object, String, Response<CompanyDetails>> {

	public CompanyDetailsTask(Context mContext) {
		super(mContext);
	}

	public CompanyDetailsTask(Context mContext, boolean showProgress) {
		super(mContext, showProgress);
	}
	
	private Response<CompanyDetails> responseData;

	@Override
	protected Response<CompanyDetails> doInBackground(Object... args) {
		responseData = protocol.getCompanyDetails();
		return responseData;
	}

	@Override
	protected void onPostExecute(Response<CompanyDetails> aBoolean) {
		super.onPostExecute(aBoolean);
	}

	public Response<CompanyDetails> getData() {
		return responseData;
	}
}
