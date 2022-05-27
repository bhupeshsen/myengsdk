package com.p4rc.sdk.crypto;

import java.util.Map;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

public class CipherPreferencesEx implements SharedPreferences {     
	protected static final String UTF8 = "utf-8";     
	private static final char[] SEKRIT = {'a','n','d','r','o','i', 'd'}; //INSERT A RANDOM PASSWORD HERE.
											// Don't use anything you wouldn't want to 
											// get out there if someone decompiled                                                
										   // your app.       
	private final String ANDROID_ID; 
	final protected SharedPreferences delegate;     
	final protected Context context;      
	
	public CipherPreferencesEx (Context context, SharedPreferences delegate) 
	{         
		this.delegate = delegate;         
		this.context = context;
		@SuppressWarnings("deprecation")
		String seed = Settings.Secure.getString(context.getContentResolver(),Settings.System.ANDROID_ID);
		this.ANDROID_ID = (seed == null ? new String(SEKRIT) : seed);  
		
	}
	
	public class Editor implements SharedPreferences.Editor 
	{         
		protected SharedPreferences.Editor delegate;          
		
		public Editor() {             
			this.delegate = CipherPreferencesEx.this.delegate.edit();                             
		}          
		
		@Override         
		public Editor putBoolean(String key, boolean value) {             
			delegate.putString(key, encrypt(Boolean.toString(value)));             
			return this;         
		}          
		
		@Override         
		public Editor putFloat(String key, float value) {             
			delegate.putString(key, encrypt(Float.toString(value)));             
			return this;         
		}          
		
		@Override         
		public Editor putInt(String key, int value) {             
			delegate.putString(key, encrypt(Integer.toString(value)));             
			return this;         
		}          
		
		@Override         
		public Editor putLong(String key, long value) {             
			delegate.putString(key, encrypt(Long.toString(value)));             
			return this;         
		}          
		
		@Override         
		public Editor putString(String key, String value) {             
			delegate.putString(key, encrypt(value));             
			return this;         
		}          
	      	
		@Override         
		public Editor clear() {             
			delegate.clear();             
			return this;         
		}          
		
		@Override         
		public boolean commit() {             
			return delegate.commit();         
		}          
		
		@Override         
		public Editor remove(String s) {             
			delegate.remove(s);             
			return this;         
		}

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void apply() {
			delegate.apply();			
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public android.content.SharedPreferences.Editor putStringSet(
				String arg0, Set<String> arg1) {
			return delegate.putStringSet(arg0, arg1);
		}     
		}      
	
	public Editor edit() {         
		return new Editor();     
	}       
	
	@Override     
	public Map<String, ?> getAll() {         
		throw new UnsupportedOperationException(); // left as an exercise to the reader     
	}      
	
	@Override     
	public boolean getBoolean(String key, boolean defValue) {         
		final String v = delegate.getString(key, null);         
		return v!=null ? Boolean.parseBoolean(decrypt(v)) : defValue;     
	}      
	
	@Override     
	public float getFloat(String key, float defValue) {         
		final String v = delegate.getString(key, null);         
		return v!=null ? Float.parseFloat(decrypt(v)) : defValue;     
	}      
	
	@Override     
	public int getInt(String key, int defValue) {         
		final String v = delegate.getString(key, null);         
		return v!=null ? Integer.parseInt(decrypt(v)) : defValue;     
	}      
	
	@Override     
	public long getLong(String key, long defValue) {         
		final String v = delegate.getString(key, null);         
		return v!=null ? Long.parseLong(decrypt(v)) : defValue;     
	}      
	
	@Override     
	public String getString(String key, String defValue) {         
		final String v = delegate.getString(key, null);         
		return v != null ? decrypt(v) : defValue;     
	}      
	
	@Override     
	public boolean contains(String s) {         
		return delegate.contains(s);     
	}      
	
	@Override     
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) 
	{         
		delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);     
	}      
	
	@Override     
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) 
	{         
		delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);     
	}         
	
	protected String encrypt( String value) {          
		try 
		{   
			//return SimpleCrypto.encrypt(new String(SEKRIT), value);
			return CryptoUtils.encrypt(ANDROID_ID, value);
		} 
		catch( Exception e ) 
		{             
			throw new RuntimeException(e);         
		}      
	}      
	
	protected String decrypt(String value){         
		try 
		{             
			//return SimpleCrypto.decrypt(new String(SEKRIT), value);
			return CryptoUtils.decrypt(ANDROID_ID, value);
		} 
		catch( Exception e) 
		{             
			throw new RuntimeException(e);         
		}     
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		return delegate.getStringSet(arg0, arg1);
	}  
}
