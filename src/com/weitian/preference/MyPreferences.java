package com.weitian.preference;

import java.io.IOException;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.weitian.WeiboSettings;
import com.weitian.V.WeiboHttpOP;
import com.weitian.err.WeiboCredentialsException;
import com.weitian.err.WeiboException;
import com.weitian.err.WeiboParseException;
import com.weitian.parsers.json.UserParser;
import com.weitian.types.Group;
import com.weitian.types.HomeTimeLine;
import com.weitian.types.User;
import com.weibo.net.*;

public class MyPreferences {
	 private static final String TAG = "Preferences";
	 private static final boolean DEBUG = WeiboSettings.DEBUG;
	 
	 public static final String TOKEN = "access_token";
	 public static final String EXPIRES = "expires_in";
	 public static final String SECRET = "secret";
	 public static final String UID = "uid";
	 
	 public static String MAIN_LIST_ID = "main_list_id";
	 
	 
	
	 /**1.保存token,过期时间,id 2.保存user对象
	  * */
	 public static boolean storeTokenAndSecret(WeiboHttpOP weiboHttpOP,final Editor editor, String token, String secret,String expires,long uid,Token accessToken) 
			 throws WeiboCredentialsException,WeiboParseException, WeiboException, IOException{
		 	if(DEBUG)Log.d(TAG, "token1:"+token+",expires:"+expires+",secret:"+secret+",uid:"+uid);
		 	editor.putString(TOKEN, token);
	        editor.putString(EXPIRES, Long.toString(accessToken.getExpiresTime()));
	        editor.putString(SECRET, secret);
	        editor.putLong(UID, uid);
	        if (!editor.commit()) {
	        	if(DEBUG)Log.d(TAG, "storeTokenAndSecret commit failed");
	            return false;
	        }
	        if(DEBUG)Log.d(TAG, "token2:"+accessToken.getToken());
	       
	        weiboHttpOP.setmAccessToken(accessToken);
	        
	        User user = weiboHttpOP.user(null, accessToken.getToken(), uid, null);
	        
	        weiboHttpOP.homeTimeLine(null,accessToken.getToken(),
 					0, 0, 20, 20, 0, 0, 0);
	        
	        storeUser(editor,user);
	        
	        if(!editor.commit()){
	        	if(DEBUG)Log.d(TAG, "storeTokenAndSecret commit failed");
	        	return false;
	        }
	        
	        return true;
	        
	 }
	 
	 public static void storeUser(final Editor editor,User user){
		 if(user!=null&&!user.getIdstr().equals("null")){
			 editor.putLong(UserParser.ID, user.getId());
			 editor.putString(UserParser.IDSTR, user.getIdstr());
			 if(DEBUG)Log.d(TAG, "store user info");
		 }else{
			 if(DEBUG)Log.d(TAG, "no store user info");
		 }
	 }
	 
	 public static boolean logoutUser(WeiboHttpOP weiboHttpOP,Editor editor){
		 if(DEBUG)Log.d(TAG, "Trying to log out");
		 weiboHttpOP.setmAccessToken(null);
		 return editor.clear().commit();
	 }
	 
	 public static String getUserIdstr(SharedPreferences prefs){
		 return prefs.getString(UserParser.IDSTR, null);
	 }
	 
	 public static Long getUserId(SharedPreferences prefs){
		 return prefs.getLong(UserParser.ID, 0);
	 }
	 
	 
	 
	 public static AccessToken getTokenAndSecret(SharedPreferences prefs) {
		 AccessToken accessToken = new AccessToken(prefs.getString(TOKEN, null),prefs.getString(SECRET, null));
		 accessToken.setExpiresTime(prefs.getString(EXPIRES, null));
		 accessToken.setUid(prefs.getLong(UID, 0));
	     return accessToken;
	 }
	 
	 public static boolean storeMainListId(final Editor editor,long id){
		 	 if(DEBUG)Log.d(TAG, "MainListId is: "+id);
			 editor.putLong(MAIN_LIST_ID,id);
			 if(!editor.commit()){
		        	if(DEBUG)Log.d(TAG, "storeMainListId commit failed");
		        	return false;
		        }
		        return true;
	 }
	 
	 public static long getMainListId(SharedPreferences prefs){
		 return prefs.getLong(MAIN_LIST_ID, 0);
	 }
}
