package com.p4rc.sdk.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.R;
import com.p4rc.sdk.model.GamePoint;
import com.p4rc.sdk.task.BatchCheckinPointsTask;
import com.p4rc.sdk.task.CustomAsyncTask;
import com.p4rc.sdk.task.GetUserInfoTask;
import com.p4rc.sdk.utils.AppUtils;
import com.p4rc.sdk.utils.Constants;
import com.p4rc.sdk.utils.JsonUtility;
import com.p4rc.sdk.utils.PointsManager;
import com.p4rc.sdk.view.CustomButton;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.webkit.WebSettings.LayoutAlgorithm.NORMAL;

public class MainActivity extends Activity implements View.OnClickListener,
        CustomAsyncTask.AsyncTaskListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private WebView myWebView;
    private String url;

    private CookieManager cookieManager;

    private GetUserInfoTask getUserInfoTask = null;
    private boolean isPrivacyPolicyTermsCondition = true;

    private ProgressBar progressBar;
    private Context context;

    private LinearLayout mainHeaderFB;
    private RelativeLayout mainHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.p4rc_main);

        AppConfig.getInstance().setContext(this);

        context = this;

        CookieSyncManager.createInstance(MainActivity.this);
        CookieSyncManager.getInstance().sync();
        cookieManager = CookieManager.getInstance();

        url = AppConfig.getInstance().getHtmlPageUrl();
        url += "#showGamersExp&p=["
                + PointsManager.getInstance(this).getLastP4RCPoints() + ","
                + PointsManager.getInstance(this).getLastLevelValue()
                + ",0,%22" + P4RC.getInstance().getGameRefId() + "%22]";

        initViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initViews() {

        mainHeader = (RelativeLayout) findViewById(R.id.main_header);
        mainHeaderFB = (LinearLayout) findViewById(R.id.main_header_fb);

        CustomButton btnClose = (CustomButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        CustomButton btnCloseFb = (CustomButton) findViewById(R.id.btn_close_fb);
        btnCloseFb.setOnClickListener(this);

//		AbsoluteLayout mainContent = (AbsoluteLayout) findViewById(R.id.main_conten);
        RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.main_conten);
//		LinearLayout mainContentHeader = (LinearLayout) findViewById(R.id.main_header);
        LayoutParams params = mainContent.getLayoutParams();
//		LayoutParams paramsHeader = mainContentHeader.getLayoutParams();

        myWebView = new WebView(this);

        AbsoluteLayout.LayoutParams frameLayoutParams = new AbsoluteLayout.LayoutParams(
                AbsoluteLayout.LayoutParams.MATCH_PARENT,
                AbsoluteLayout.LayoutParams.MATCH_PARENT, 0, 0);


        mainContent.addView(myWebView, frameLayoutParams);
        mainContent.invalidate();
        mainContent.bringChildToFront(btnClose);

        progressBar = new ProgressBar(this);

        int orient = getResources().getConfiguration().orientation;
        if (orient == Configuration.ORIENTATION_LANDSCAPE) {
//			setParamOrientationLandscape(params, paramsHeader);
            setParamOrientationLandscape(params, null);
        } else {
//			setParamOrientationPortrait(params, paramsHeader);
            setParamOrientationPortrait(params, null);
        }

        RelativeLayout.LayoutParams bar_params = new RelativeLayout.LayoutParams(100, 100);
        bar_params.addRule(RelativeLayout.CENTER_IN_PARENT);

        mainContent.addView(progressBar, bar_params);

        myWebView.clearHistory();
        myWebView.clearFormData();
        myWebView.clearCache(true);

        AppUtils.setupSessiodIdInCookie(this, url);
        String cookie = cookieManager.getCookie(url);
        Log.d(TAG, "cookies after set sessionId = " + cookie);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(NORMAL);

        // Needed for proper scaling (enables view port tag).
        webSettings.setUseWideViewPort(true);

        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("P4RC_WEB_VIEW", message + " -- From line "
                        + lineNumber + " of "
                        + sourceID);
            }
        });
        myWebView.loadUrl(url);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//		AbsoluteLayout mainContent = (AbsoluteLayout) findViewById(R.id.main_conten);
        RelativeLayout mainContent = (RelativeLayout) findViewById(R.id.main_conten);
//		LinearLayout mainContentHeader = (LinearLayout) findViewById(R.id.main_header);

        LayoutParams params = mainContent.getLayoutParams();
//		LayoutParams paramsHeader = mainContentHeader.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//			setParamOrientationLandscape(params, paramsHeader);
            setParamOrientationLandscape(params, null);
        } else {
//			setParamOrientationPortrait(params, paramsHeader);
            setParamOrientationPortrait(params, null);
        }
        myWebView.invalidate();
    }

    private void setParamOrientationLandscape(LayoutParams params,
                                              LayoutParams paramsHeader) {
        //params.height = (int) AppUtils.convertDpToPixel(290.0f, this);
        //params.width = (int) AppUtils.convertDpToPixel(480.0f, this);
//		paramsHeader.width = (int) AppUtils.convertDpToPixel(480.0f, this);
        //progressBar.setPadding((int) AppUtils.convertDpToPixel(215.0f, this),(int) AppUtils.convertDpToPixel(105.0f, this), 0, 0);
    }

    private void setParamOrientationPortrait(LayoutParams params,
                                             LayoutParams paramsHeader) {
        //params.height = (int) AppUtils.convertDpToPixel(450.0f, this);
        //params.width = (int) AppUtils.convertDpToPixel(320.0f, this);
//		paramsHeader.width = (int) AppUtils.convertDpToPixel(320.0f, this);

        //progressBar.setPadding((int) AppUtils.convertDpToPixel(135.0f, this),(int) AppUtils.convertDpToPixel(185.0f, this), 0, 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView != null
                && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        myWebView.stopLoading();
        finish();
    }

    private void processGetUserInfo() {
        if (getUserInfoTask == null) {
            getUserInfoTask = new GetUserInfoTask(this);
            getUserInfoTask.setShowProgress(true);
            getUserInfoTask.setAsyncTaskListener(this);
            getUserInfoTask.execute();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoadnig: url = " + url);
//			if (url.contains("facebook.com")){
//				mainHeader.setVisibility(View.GONE);
//				mainHeaderFB.setVisibility(View.VISIBLE);
//			} else{
//				mainHeaderFB.setVisibility(View.GONE);				
//				mainHeader.setVisibility(View.VISIBLE);
//			}
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted: url = " + url);
            if (url.contains("facebook.com")) {
                mainHeader.setVisibility(View.GONE);
                mainHeaderFB.setVisibility(View.VISIBLE);
            } else {
                mainHeaderFB.setVisibility(View.GONE);
                mainHeader.setVisibility(View.VISIBLE);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (url.equals(AppConfig.PRODUCTION_SERVER_HOST + "/services/v1/user/logout")
                    || url.equals("https://www.facebook.com/logout.php")) {
                P4RC.getInstance().logout();
            }
            if (url.equals("http://terms.pretio.in/")
                    || url.equals("http://privacy.pretio.in/")
                    || url.equals("http://reward-terms.pretio.in/")) {
                if (isPrivacyPolicyTermsCondition) {
                    view.loadUrl(url);
                    isPrivacyPolicyTermsCondition = false;
                }
            }
            if (url.startsWith(AppConfig.PRODUCTION_SERVER_HOST + "/mIndex.html")) {
                isPrivacyPolicyTermsCondition = true;
            }
            if (url.startsWith("https://play.google.com/store/apps/")) {
                view.stopLoading();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: url = " + url);

            String sessionId = null;
            if (cookieManager.hasCookies()) {
                String sessionCookie = cookieManager.getCookie(url);
                Log.d(TAG, "sessionCookie = " + sessionCookie);
                if (sessionCookie != null) {
                    Map<String, String> cookiesMap = AppUtils
                            .parseCookies(sessionCookie);
                    Log.d(TAG, cookiesMap.keySet().toString());
                    if (cookiesMap
                            .containsKey(Constants.SESSION_ID_COOKIE_PARARM)) {
                        sessionId = URLDecoder.decode(cookiesMap
                                .get(Constants.SESSION_ID_COOKIE_PARARM));
                        Log.d(TAG, "After load page sessionId= " + sessionId);
                        try {
                            processGetUserInfo();
                        } catch (Exception e) {
                            Log.d(TAG, "cancel load");
                        }
                    } else {
                        sessionId = null;
                    }
                } else {
                    sessionId = null;
                }

            } else {
                sessionId = null;
                Log.d(TAG, "Cookies is empty");
            }

            if (sessionId != null) {
                AppConfig.getInstance().setSessionId(sessionId);
                final ArrayList<GamePoint> gamePointsArrayList = PointsManager
                        .getInstance(AppConfig.getInstance().getContext())
                        .getGamePointsTable();

                if (gamePointsArrayList != null && gamePointsArrayList.size() > 0) {
                    BatchCheckinPointsTask batchTask = new BatchCheckinPointsTask(
                            MainActivity.this, false);
                    batchTask.setAsyncTaskListener(new BatchTaskListener());
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        batchTask.execute();
                    } else {
                        batchTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Object());
                    }
                }
            }

            AppConfig.getInstance().setSessionId(sessionId);

            stopWaitLoad();
        }
    }

    private void stopWaitLoad() {
        progressBar.setVisibility(View.GONE);
    }

    private class BatchTaskListener implements
            CustomAsyncTask.AsyncTaskListener {

        @Override
        public void onBeforeTaskStarted(CustomAsyncTask<?, ?, ?> task) {
        }

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

    @Override
    public void onBeforeTaskStarted(CustomAsyncTask<?, ?, ?> task) {
    }

    @Override
    public void onTaskFinished(CustomAsyncTask<?, ?, ?> task) {
        HashMap<String, Object> responseData = ((GetUserInfoTask) task)
                .getData();
        if (JsonUtility.SUCCESS_STATUS.equals(responseData
                .get(JsonUtility.STATUS_PARAM))) {
        }

        getUserInfoTask = null;
    }
}
