package com.coderivium.p4rcintegrationsample;


import android.content.Context;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.P4RC;

public class Application extends android.app.Application {
    public static final String TAG = Application.class.getSimpleName();
    private static Application instance;
    private Context context;

    public static Application getInstance() {
        if (instance == null) {
            Log.d(TAG, "Application instance is null!");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        context = getApplicationContext();

        FacebookSdk.sdkInitialize(context);

        P4RC.getInstance().setContext(context);
        P4RC.getInstance().initialize(BuildConfig.GAME_REFERENCE, BuildConfig.API_KEY, AppConfig.PRODUCTION_SERVER_HOST);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "Application: onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "Application: onTerminate");
        super.onTerminate();
    }
}
