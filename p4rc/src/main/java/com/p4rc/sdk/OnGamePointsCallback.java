package com.p4rc.sdk;

import com.p4rc.sdk.model.gamelist.GameList;
import com.p4rc.sdk.model.gamelist.GamePoints;

/**
 * Created by bhupesh on 14/06/22.
 */
public interface OnGamePointsCallback {

    void onSuccess(GamePoints userPoints);

    void onError(int errorCode, String message);

}
