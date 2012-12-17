package com.weitian;

import com.weibo.net.Token;
import com.weitian.V.WeiboHttpOP;
import com.weitian.preference.MyPreferences;
import com.weitian.util.NotificationsUtil;
import com.weitian.util.ResourceManager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.weibo.net.*;

public class Weitian extends Application {
	
	private static final String TAG = "weitian";
    private static final boolean DEBUG = WeiboSettings.DEBUG;
	
	public static final String PACKAGE_NAME = "com.weitian";
	
	public static final String INTENT_ACTION_LOGGED_OUT = "com.weitian.intent.action.LOGGED_OUT";
	public static final String INTENT_ACTION_LOGGED_IN = "com.weitian.intent.action.LOGGED_IN";
	
	private SharedPreferences mPrefs;
	
	private WeiboHttpOP mWerboHttpOP = null;
	
	private Token mAccessToken = null;
	
	private String mVersion = null;
	
	private ResourceManager mResourceManager;
	
	private long ExpiresTime;
	
	private int widthPixels;
	private int heightPixels;
	private float density;
	private int screenWidth;
	private int screenHeight;
	
	public long getExpiresTime() {
		return ExpiresTime;
	}


	public void setExpiresTime(long expiresTime) {
		ExpiresTime = expiresTime;
	}
	
	public int getWidthPixels() {
		return widthPixels;
	}


	public int getHeightPixels() {
		return heightPixels;
	}


	public float getDensity() {
		return density;
	}


	public int getScreenWidth() {
		return screenWidth;
	}


	public int getScreenHeight() {
		return screenHeight;
	}

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mVersion = getVersionString(this);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		loadWeiboHttpOP();
		loadResourceManagers();
		getScreenPixels();
		
	}

	
	public void getScreenPixels(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
        widthPixels= dm.widthPixels;
        heightPixels= dm.heightPixels;
        density = dm.density;
        screenWidth = (int) (widthPixels*density);
        screenHeight = (int) (heightPixels*density);
        Log.d(TAG, "screenWidth:"+screenWidth+"screenHeight:"+screenHeight);
	}
	
	public WeiboHttpOP getWeiboHttpOP(){
		if(DEBUG)Log.d(TAG, "getWeiboHttpOP");
		return mWerboHttpOP;
	}
	
	public void loadWeiboHttpOP() {
		if(DEBUG)Log.d(TAG, "TXloadWeiboHttpOP");
		if(mWerboHttpOP==null){
			mWerboHttpOP = new WeiboHttpOP(WeiboHttpOP.createHttpApi(mVersion,true,this));
		}
		mAccessToken = new AccessToken(mPrefs.getString(MyPreferences.TOKEN, "0"),mPrefs.getString(MyPreferences.SECRET, null));
		mAccessToken.setExpiresTime(Long.parseLong(mPrefs.getString(MyPreferences.EXPIRES, "0")));
		mWerboHttpOP.setmAccessToken(mAccessToken);	
	}
	
	public boolean isReady(){
		Log.d(TAG, "!!!"+getWeiboHttpOP().getmAccessToken().getExpiresTime()+" "+System.currentTimeMillis());
		return getWeiboHttpOP().hasmAccessToken() && !TextUtils.isEmpty(getUserId())&& getWeiboHttpOP().getmAccessToken().getExpiresTime()>System.currentTimeMillis();
	}
	
	public String getUserId(){
		return MyPreferences.getUserIdstr(mPrefs);
	}

	private static String getVersionString(Context context) {
        // Get a version string for the app.
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
            return PACKAGE_NAME + ":" + String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            if (DEBUG) Log.d(TAG, "Could not retrieve package info", e);
            throw new RuntimeException(e);
        }
    }
	
	public ResourceManager getRemoteResourceManager() {
        return mResourceManager;
    }
	
	private void loadResourceManagers() {
		try {
            mResourceManager = new ResourceManager("cache");
        } catch (IllegalStateException e) {
        	
            NotificationsUtil.ToastReasonForFailure(this, e);
        }
	}

}
