package com.p4rc.sdk;

import com.p4rc.sdk.model.CompanyDetails;
import com.p4rc.sdk.model.gamelist.GameList;

/**
 * Created by alla on 5/10/16.
 */
public interface OnCompanyDetailsCallback {

    void onSuccess(CompanyDetails data);

    void onError(int errorCode, String message);

}
