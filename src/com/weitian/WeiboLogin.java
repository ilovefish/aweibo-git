package com.weitian;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.weibo.net.*;
import com.weitian.V.WeiboHttpOP;
import com.weitian.err.WeiboException;
import com.weitian.preference.MyPreferences;
import com.weitian.util.NotificationsUtil;

public class WeiboLogin extends Activity{
	
	/*Weibo*/
	private Weibo mWeibo = null;
	private Token mAccessToken = null;
	private LoginTask mLoginTask = null;
	
	/*Authorize parameter*/
	public static final String UID = "uid";
	private static final String KEY = "4008560357";
	private static final String SECRET = "645017fd8bc0c8cfbbb3a05038811c3a";
	private static final String CALLBACK_URL = "http://weibo.com/";
	private String mToken = null;
	private String mExpires_in = null;
	private long mId = 0;
	
	/*debug parameter*/
	public static final String TAG = "WeiboLogin";
	public static final boolean DEBUG = WeiboSettings.DEBUG;
	
	/**@function show a ProgressDialog*/
	private ProgressDialog mProgressDialog = null;
	private ProgressDialog showProgressDialog(){
		if(mProgressDialog == null){
			ProgressDialog dialog = new ProgressDialog(this);
//			dialog.setTitle(R.string.login_dialog_title);
			dialog.setMessage("Loading...");
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setIndeterminate(true);
            dialog.setCancelable(true);
			mProgressDialog = dialog;
		}
		mProgressDialog.show();
		return mProgressDialog;
	}
	
	private void dismissProgressdialog(){
		if(mProgressDialog!=null){
			mProgressDialog.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(DEBUG){
			Log.d(TAG,"onCreate");
		}
		
		this.setContentView(R.layout.login_activity);
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(DEBUG)Log.d(TAG,"onPause");
			if(mLoginTask!=null){
				mLoginTask.cancel(true);
		}
	}

	
	public void onClick(View view){
		
		mWeibo = Weibo.getInstance();
		mWeibo.setupConsumerConfig(KEY, SECRET);
		mWeibo.setRedirectUrl(CALLBACK_URL);
		mWeibo.authorize(WeiboLogin.this,
				new AuthDialogListener());
		
//		//首选项界面
//		Intent intent = new Intent().setClass(this,WeiboPreferenceActivity.class);
//		this.startActivity(intent);
//		Log.v(TAG, "onclick");
	}
	
	/**@function 回调函数，返回请求用户权限*/
	private class AuthDialogListener implements WeiboDialogListener{

		public static final String TAG = "AuthDialogListener";
		public static final boolean DEBUG = WeiboSettings.DEBUG;
		
		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			mToken = values.getString("access_token");//access_token
			mExpires_in = values.getString("expires_in");//access_token的生命周期
			mId = Long.parseLong(values.getString(UID));
			
			if(DEBUG)Log.d(TAG, "mToken:"+mToken+"mExpires_in:"+mExpires_in+"mId:"+mId);
			mAccessToken = new AccessToken(mToken, SECRET);
			mAccessToken.setExpiresTime(mExpires_in);
			mAccessToken.setUid(mId);
			mLoginTask = new LoginTask(WeiboLogin.this);
			mLoginTask.execute();
			
		}

		public void onWeiboException(WeiboException e) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
			
		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onWeiboException(com.weibo.net.WeiboException e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Boolean> {
		
    	/*有关调试的参数*/
        private static final String TAG = "WeiboLogin_LoginTask";
        private static final boolean DEBUG = WeiboSettings.DEBUG;
        private SharedPreferences prefs = null;
        private WeiboLogin mWeiboLogin = null;

        /*provide NotificationsUtil Object produce Toast*/
        private Exception mReason;

        public LoginTask(WeiboLogin weiboLogin){
        	mWeiboLogin = weiboLogin;
        }
        
        public void setWeiboLogin(WeiboLogin weiboLogin){
        	mWeiboLogin = weiboLogin;
        }
        
        /**
         * @return 
         * @function show Progress Dialog for login
         * */
        protected void onPreExecute() {
            if (DEBUG) Log.d(TAG, "onPreExecute()");
            showProgressDialog();
            return;
        }

        /**
         * @function 
         * */
        protected Boolean doInBackground(Void... params) {
            if (DEBUG) Log.d(TAG, "doInBackground()");
            Weitian weitian = (Weitian) getApplication(); 
            WeiboHttpOP weiboHttpOP = weitian.getWeiboHttpOP();
            
            prefs = PreferenceManager
            		.getDefaultSharedPreferences(mWeiboLogin);
            Editor editor = prefs.edit();
            try {
				boolean loggedIn = MyPreferences.storeTokenAndSecret(weiboHttpOP,editor, mToken, SECRET, mExpires_in,mId,mAccessToken);
				String token = MyPreferences.getTokenAndSecret(prefs).getToken();
            	if(TextUtils.isEmpty(token)){
            		if (DEBUG) Log.d(TAG, "Preference store calls failed");
            	}
            	if(DEBUG) Log.d(TAG, "Preference store calls success token3:"+token);
				String userId = MyPreferences.getUserIdstr(prefs);
				if(TextUtils.isEmpty(userId)){
					if(DEBUG)Log.d(TAG, "Preference store calls failed");
					throw new WeiboException(getResources().getString(
                            R.string.login_failed_login_toast));
				}
				return loggedIn;
			} catch (Exception e) {
				 if (DEBUG) Log.d(TAG, "Caught Exception logging in.", e);
	                mReason = e;
	             MyPreferences.logoutUser(weiboHttpOP, editor);
	             return false;
			}
        }

        
        /**
         * @function handle something after login(success) or logout(fail)
         * */
        protected void onPostExecute(Boolean loggedIn) {
            if (DEBUG) Log.d(TAG, "onPostExecute(): " + loggedIn);
            if(loggedIn){
            	LogIn();
            }else{
            	
            }
            dismissProgressdialog();
            
        }

        private void LogIn(){
        	Log.d("LogIn", "TXAccessToken:"+((Weitian)getApplication()).getWeiboHttpOP().getmAccessToken().getToken()+" WeiboHttpOP:"+((Weitian)getApplication()).getWeiboHttpOP());
        	Intent intent = new Intent(mWeiboLogin,MainActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(intent);
        	finish();
        }
        
        private void LogOut(){
        	sendBroadcast(new Intent(Weitian.INTENT_ACTION_LOGGED_OUT));
        	NotificationsUtil.ToastReasonForFailure(WeiboLogin.this, mReason);
        }
        /**
         * @function dismiss Progress Dialog for login finished
         * */
        protected void onCancelled() {
            dismissProgressdialog();
        }
    }
	
	
}
