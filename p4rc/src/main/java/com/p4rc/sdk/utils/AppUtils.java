package com.p4rc.sdk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.p4rc.sdk.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class AppUtils {

	private static final float TABLET_DISPLAY_SIZE = 6f;
	
	public static Map<String, String> parseCookies(String cookies){
		Map<String, String> result = new HashMap<String, String>();
			for (String cookie : cookies.split(";")) {
				String[] pair = cookie.split("=");
				String key = pair[0].trim();
				if(key.equals(Constants.SESSION_ID_COOKIE_PARARM)){
					String value = pair[1].trim();
					result.put(key, value);
				}
			}
		return result;
	}
	

	public static void setupSessiodIdInCookie(Context context, String url){
		if (AppConfig.getInstance().getSessionId() != null){			
			CookieSyncManager.createInstance(context);
			String sessionCookie = CookieManager.getInstance().getCookie(url);
			CookieManager.getInstance().setAcceptCookie(true);
			
			CookieManager.getInstance().removeSessionCookie();
			
			StringBuilder cookieBuilder = new StringBuilder();
			cookieBuilder.append(Constants.SESSION_ID_COOKIE_PARARM);
			cookieBuilder.append("=");
			cookieBuilder.append(URLEncoder.encode(AppConfig.
					getInstance().getSessionId()));
			cookieBuilder.append(Constants.DOT_WITH_COMMA);
			cookieBuilder.append(Constants.DOMAIN_PART);
			cookieBuilder.append(getDomain(url));
			cookieBuilder.append(Constants.DOT_WITH_COMMA);
			cookieBuilder.append(Constants.EXPIRES_PART);
			cookieBuilder.append(getCookieExpireTime(System.currentTimeMillis()));
			cookieBuilder.append(Constants.DOT_WITH_COMMA);
			cookieBuilder.append(Constants.PATH_PART);
			
			if (sessionCookie != null && sessionCookie.contains(URLEncoder.encode(AppConfig.
					getInstance().getSessionId()))){
				sessionCookie=cookieBuilder.toString() + "; " + sessionCookie; 
			} else{
				sessionCookie = cookieBuilder.toString();
			}
			Log.d("AppUtils","set cookie = " + sessionCookie);
			CookieManager.getInstance().setCookie(getURLForDomain(url), sessionCookie);
			CookieSyncManager.getInstance().sync();
		}
	}
	

    public static boolean isOnline(Context ctx) {
        NetworkInfo netInfo = ((ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    
    public static long getTimeInGMT() {
    	long currentTime = System.currentTimeMillis();
    	TimeZone zone = TimeZone.getDefault();
    	return currentTime - zone.getOffset(currentTime);
    }
    
    public static Calendar unixToCalendar(long unixTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        return calendar;
    }
    
	public static long getCurrentTimeMiliss() {
		return System.currentTimeMillis() / 1000L;
	}
	
	public static boolean canMakeCall(Context ctx) {
		TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.
				TELEPHONY_SERVICE);
		return manager.getSimState() == TelephonyManager.SIM_STATE_READY &&
				(manager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM ||
						manager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA);
	}
	
	public static boolean isTabletScreen(Activity cnt) {
		Display display = cnt.getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		float height = metrics.heightPixels / metrics.xdpi;
		float width = metrics.widthPixels / metrics.ydpi;
		double diagonal = Math.sqrt(height * height + width * width);
		return diagonal >= TABLET_DISPLAY_SIZE;
	}
	
	public static String getFormatedTimeString(long timeStamp) {
		Date date = new Date(timeStamp);
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.TIME_FORMAT);
		return formatter.format(date);
	}
	
	public static String getCookieExpireTime(long expireTime) {
		long additionaTime = 1000*60*60*24;
		expireTime += additionaTime;
		Date date = new Date(expireTime);
		SimpleDateFormat form = new SimpleDateFormat(Constants.COOKIE_EXPIRE_TIME_FORMAT, Locale.ENGLISH);
		String ret = form.format(date) + Constants.GMT_PART;
		return ret;
	}
	
	public static String getDomain(String url) {
		URL urlConverter = null;
		String domain = "";
		try {
			urlConverter = new URL(url);
			if(urlConverter != null) {
				String host = urlConverter.getHost();
				if(host.contains(":")) {
					domain = urlConverter.getHost().substring(0, 
							(urlConverter.getHost().indexOf(":")));
				} else {
					domain = host;
				}
			}
		} catch (MalformedURLException e) {}
		return domain;
	}
	
	public static String getURLForDomain(String url) {
		URL urlConverter = null;
		StringBuilder builder = new StringBuilder();
		try {
			urlConverter = new URL(url);
			if(urlConverter != null) {
				builder.append(urlConverter.getProtocol());
				builder.append("://");
				if(urlConverter.getHost().contains(":")) {
					builder.append(urlConverter.getHost().substring(0, 
							(urlConverter.getHost().indexOf(":"))));
				} else {
					builder.append(urlConverter.getHost());
				}
				
			}
		} catch (MalformedURLException e) {}
		return builder.toString();
	}
	
	public static String getTransTime(long timeStamp) {
		return getFormatedTimeString(timeStamp);
	}
	
	public static AlertDialog getComplexAlertDialog(String title, String message, String posBtnText,
			String negBtnText, OnClickListener posBtnListener, 
			OnClickListener negBtnListener, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton(posBtnText, posBtnListener);
		builder.setNegativeButton(negBtnText, negBtnListener);
		return builder.create();
	}
	
	public static String mergeZipAndCity(String zip, String city) {
		StringBuilder builder = new StringBuilder();
		builder.append(zip);
		builder.append(ValidationUtils.ONE_SPACE);
		builder.append(city);
		return builder.toString();
	}
    
    public static void showAlert(Context ctx, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("Alert");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		builder.show();
    }
    
    public static void showAlert(Context cnt, String title, String message,
    		OnClickListener buttonListener) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", buttonListener);
		builder.show();
    }
    
    public static AlertDialog getCustomAlert(Context cnt, String title, String msg, String posBtnText,
    		String negBtnText, OnClickListener posListener, OnClickListener negListener) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
    	if(title != null) {
    		builder.setTitle(title);
    	}
    	if(msg != null) {
    		builder.setMessage(msg);
    	}
    	if(negListener != null && negBtnText != null) {
    		builder.setNegativeButton(negBtnText, negListener);
    	}
    	if(posListener != null && posBtnText != null) {
    		builder.setPositiveButton(posBtnText, posListener);
    	}
    	return builder.create();
    }

    
    public static void showAlert(Context cnt, String title, String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
    }
    
    public static void showAlert(Context cnt, int title, int message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(cnt);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
    }
    
    public static void showAlert(Context ctx, int resId) {
    	String message = ctx.getString(resId);
    	showAlert(ctx, message);
    }
    
    public static void showToast(Context ctx, String message) {
    	Toast tost = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
    	tost.setGravity(Gravity.CENTER, 0, 0);
    	tost.show();
    }	
    
    public static void showToast(Context ctx, int resId) {
    	String message = ctx.getString(resId);
    	showToast(ctx, message);
    }
    
	public static Bitmap loadBitmapFromView(View v) {
	    Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	    Canvas c = new Canvas(b);
	    v.measure(MeasureSpec.makeMeasureSpec(v.getLayoutParams().width, MeasureSpec.EXACTLY),
	            MeasureSpec.makeMeasureSpec(v.getLayoutParams().height, MeasureSpec.EXACTLY));
	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	    v.draw(c);
	    return b;
	}
		
	
	public static String doubleToString(double value){
		NumberFormat formatter = new DecimalFormat("0.##");
		return formatter.format(value);
	}
	
	public static float roundTwoDecimals(float f)
	{
		return (float)Math.round(f * 100) / 100;
	}
	
    public static void clearCookies(Context context) {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
            CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
    
	public static String getFirstChar(String value){
		if(value != null) {
			for(int i = 0; i< value.length(); i++){
				char ch = value.charAt(i);
				if (Character.isLetter(ch)){
					return String.valueOf(ch).toUpperCase();
				}
			}
		}
		return String.valueOf(" ");
	}
	
	public static int getPixels(Context context, int dipValue){
        Resources r = context.getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
        return px;
	}
	
	
	public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	public static float convertPixelsToDp(float px,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;

	}

	/**
	 * Given either a Spannable String or a regular String and a token, apply
	 * the given CharacterStyle to the span between the tokens, and also remove
	 * tokens.
	 * <p>
	 * For example, {@code setSpanBetweenTokens("Hello ##world##!", "##",
	 * new ForegroundColorSpan(0xFFFF0000));} will return a CharSequence
	 * {@code "Hello world!"} with {@code world} in red.
	 * 
	 * @param text
	 *            The text, with the tokens, to adjust.
	 * @param token
	 *            The token string; there should be at least two instances of
	 *            token in text.
	 * @param cs
	 *            The style to apply to the CharSequence. WARNING: You cannot
	 *            send the same two instances of this parameter, otherwise the
	 *            second call will remove the original span.
	 * @return A Spannable CharSequence with the new style applied.
	 * 
	 * @see {http://developer.android.com/reference/android/text/style/CharacterStyle.html}
	 */

	public static CharSequence setSpanBetweenTokens(CharSequence text, String token, CharacterStyle... cs) {
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1) {
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}

		return text;
	}

	public static boolean loadFile(final String url, final File f) throws IOException {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				URL imageUrl;
				try {
					imageUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
					conn.setConnectTimeout(30000);
					conn.setReadTimeout(30000);
					conn.setInstanceFollowRedirects(true);
					InputStream is = conn.getInputStream();
					OutputStream os = new FileOutputStream(f);
					CopyStream(is, os);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	public static void closeAllBelowActivities(Activity current) {
	    boolean flag = true;
	    Activity below = current.getParent();
	    if (below == null)
	        flag = false;
	    while (flag) {
	        Activity temp = below;
	        try {
	            below = temp.getParent();
	            temp.finish();
	        } catch (Exception e) {
	            flag = false;
	        }
	    }
	    current.finish();
	}

	public static String join(Iterator<Object> iterator, String separator) {
	    // handle null, zero and one elements before building a buffer 
	    Object first = iterator.next();
	    if (!iterator.hasNext()) {
	        return first.toString();
	    }
	    // two or more elements 
	    StringBuffer buf = 
	        new StringBuffer(256); // Java default is 16, probably too small 
	    if (first != null) {
	        buf.append(first);
	    }
	    while (iterator.hasNext()) {
	        if (separator != null) {
	            buf.append(separator);
	        }
	        Object obj = iterator.next();
	        if (obj != null) {
	            buf.append(obj);
	        }
	    }
	    return buf.toString();
	}
}
