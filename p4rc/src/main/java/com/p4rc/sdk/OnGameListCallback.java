package com.p4rc.sdk;

import com.p4rc.sdk.model.gamelist.GameList;

/**
 * Created by alla on 5/10/16.
 */
public interface OnGameListCallback {

    void onSuccess(GameList gameList);

    void onError(int errorCode, String message);

}
