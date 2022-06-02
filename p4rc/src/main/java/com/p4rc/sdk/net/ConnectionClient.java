package com.p4rc.sdk.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;


public class ConnectionClient {
	
	public static final String TAG = "ConnectionClient";
	
	public static final int STATUS_CODE_OK = 200;
	
	public static final String LAST_ERROR_CODE = "lastErrorCode";
	public static final String SERVER_RESPONSE_CODE = "responseCode";
	
	protected final String CONTENT_TYPE = "application/json";
	protected final String JSON_ENTITY = "JSON: ";
	protected final String TYPE_HEADER = "Content-Type";
	protected final String ACCEPT_HEADER = "Accept";
	protected final String HTTP_SCHEME = "http";
	protected final String HTTPS_SCHEME = "https";
	
	protected final int HTTP_SCHEME_PORT = 80;
	protected final int HTTPS_SCHEME_PORT = 443;
	
	protected int lastErrorCode;
	protected int lastServerResponseCode;
	public int getLastErrorCode() {
		return lastErrorCode;
	}
	
	public void setResetLastErrorCode(){
		lastErrorCode = NetworkErrors.NO_ERROR;
	}
	
	public int getLastServerResponseStatus() {
		return lastServerResponseCode;
	}

	public String makeRequestToServer(String uri, String method, String json) {
		return makeRequestToServer(uri, method, json, null);
	}

	public String makeRequestToServer(String uri, String method, String json, Map<String, String> headers) {

		BufferedReader reader = null;
		try {
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			if (!method.equals("GET")) {
				con.setDoOutput(true);
				con.setDoInput(true);
			}
			con.setChunkedStreamingMode(0);

			if (headers != null) {
				for(String key: headers.keySet()) {
					con.setRequestProperty(key, headers.get(key));
				}
			}
			con.setRequestProperty("Content-Type", "application/json");
			if (json != null) {
				con.setDoOutput(true);
				OutputStreamWriter writer =
						new OutputStreamWriter(con.getOutputStream());
				writer.write(json);
				writer.flush();
			}

			Log.d(TAG, "makeRequestToServer: Executed");

			if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				lastErrorCode = NetworkErrors.NO_ERROR;
				lastServerResponseCode = NetworkErrors.SERVER_RESPONSE_OK;
				String line;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}

				return sb.toString();
			} else {
				Log.d(TAG, "Error Code: " + con.getResponseCode() + " " + con.getResponseMessage());
				lastErrorCode = NetworkErrors.CLIENT_PROTOCOL_EXCEPTION;
				Log.d(TAG, "err1: " + lastErrorCode);
				return null;
			}

		} catch (ProtocolException e) {
			e.printStackTrace();
			Log.d(TAG, "err2: " + e.getMessage());
			lastErrorCode = NetworkErrors.CLIENT_PROTOCOL_EXCEPTION;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.d(TAG, "err3: " + e.getMessage());
			lastErrorCode = NetworkErrors.MALFORMED_URL_EXCEPTION;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "err4: " + e.getMessage());
			lastErrorCode = NetworkErrors.IO_EXCEPTION;
		} finally {
			Log.d(TAG, "err5: " + lastErrorCode);
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			Log.d(TAG, "err6: " + lastErrorCode);

		}
		Log.d(TAG, "err7: " + lastErrorCode);

		return null;
	}
}
